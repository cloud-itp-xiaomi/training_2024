// server.go
package main

import (
    "context"
    "database/sql"
    "encoding/json"
    "fmt"
    "log"
    "net/http"
    "time"

    "github.com/go-redis/redis/v8"
    _ "github.com/go-sql-driver/mysql"
)

var (
	ctx context.Context
	rdb *redis.Client
	db  *sql.DB
)

func initialize() error {
	ctx = context.Background()

	rdb = redis.NewClient(&redis.Options{
		Addr: "127.0.0.1:6379",
	})
	if err := rdb.Ping(ctx).Err(); err != nil {
		return fmt.Errorf("error occurred during Redis connection: %w", err)
	}

	var err error
	db, err = sql.Open("mysql", "root:123456@tcp(localhost:3306)/MyDBserver")
	if err != nil {
		return fmt.Errorf("error occurred during MySQL connection: %w", err)
	}

	return nil
}


// uploadMetrics函数处理上传度量指标的HTTP请求
func uploadMetrics(w http.ResponseWriter, r *http.Request) {
    var metrics []Metric
    err := json.NewDecoder(r.Body).Decode(&metrics)
    if err != nil {
        log.Printf("Error decoding JSON: %s\n", err.Error())
        http.Error(w, err.Error(), http.StatusBadRequest)
        return
    }

    for _, metric := range metrics {
        _, err := db.Exec("INSERT INTO metrics (metric, endpoint, timestamp, step, value) VALUES (?, ?, ?, ?, ?)",
            metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value)
        if err != nil {
            log.Printf("Error inserting into database: %s\n", err.Error())
            http.Error(w, err.Error(), http.StatusInternalServerError)
            return
        }

        jsonMetric, err := json.Marshal(metric)
        if err != nil {
            log.Printf("Error marshaling metric: %s\n", err.Error())
            http.Error(w, err.Error(), http.StatusInternalServerError)
            return
        }
        rdb.LPush(ctx, metric.Metric, jsonMetric)
        rdb.LTrim(ctx, metric.Metric, 0, 9)
    }

    fmt.Printf("成功接收到 %d 条度量指标数据\n", len(metrics))

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(map[string]string{"message": "ok"})
}


// queryMetrics函数处理查询度量指标的HTTP请求
func queryMetrics(w http.ResponseWriter, r *http.Request) {
    endpoint := r.URL.Query().Get("endpoint")
    metric := r.URL.Query().Get("metric")
    startTs := r.URL.Query().Get("start_ts")
    endTs := r.URL.Query().Get("end_ts")

    query := "SELECT metric, endpoint, timestamp, step, value FROM metrics WHERE endpoint = ? AND timestamp BETWEEN ? AND ?"
    args := []interface{}{endpoint, startTs, endTs}
    if metric != "" {
        query += " AND metric = ?"
        args = append(args, metric)
    }

    rows, err := db.Query(query, args...)
    if err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }
    defer rows.Close()

    var result []Metric
    for rows.Next() {
        var metric Metric
        if err := rows.Scan(&metric.Metric, &metric.Endpoint, &metric.Timestamp, &metric.Step, &metric.Value); err != nil {
            http.Error(w, err.Error(), http.StatusInternalServerError)
            return
        }
        result = append(result, metric)
    }

    fmt.Printf("查询结果: %+v\n", result)

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(result)
}


func main() {
    err := initialize()
    if err != nil {
        log.Printf("Initialization failed: %s", err)
        return
    } else {
        log.Println("Initialization succeeded")
    }

    go func() {
        for {
            metrics,err:= collectMetrics()
            if err != nil {
                log.Printf("Failed to collect metrics: %s", err)
                continue
            }
            if err := reportMetrics(metrics); err != nil {
                log.Printf("Failed to report metrics: %s", err)
                continue
            }
            fmt.Println(metrics)
            time.Sleep(60 * time.Second)
        }
    }()

    http.HandleFunc("/api/metric/upload", uploadMetrics)
    http.HandleFunc("/api/metric/query", queryMetrics)
    if err := http.ListenAndServe(":8080", nil); err != nil {
        log.Printf("HTTP server failed: %s", err)
    }
}