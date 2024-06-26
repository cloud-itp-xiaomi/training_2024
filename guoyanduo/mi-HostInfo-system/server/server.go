package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/go-redis/redis/v8"
	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
	"golang.org/x/net/context"
)

var (
	db  *sql.DB
	rdb *redis.Client
	ctx = context.Background()
)

type Metric struct {
	Metric    string            `json:"metric"`
	Endpoint  string            `json:"endpoint"`
	Timestamp int64             `json:"timestamp"`
	Step      int64             `json:"step"`
	Value     float64           `json:"value"`
	Tags      map[string]string `json:"tags,omitempty"`
}

func initDB() {
	if err := godotenv.Load(".env"); err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	// 隐藏数据库连接信息，从.env文件读取
	dbUser := os.Getenv("DB_USER")
	dbPassword := os.Getenv("DB_PASSWORD")
	dbHost := os.Getenv("DB_HOST")
	dbPort := os.Getenv("DB_PORT")
	dbName := os.Getenv("DB_NAME")

	dsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s", dbUser, dbPassword, dbHost, dbPort, dbName)

	db, err := sql.Open("mysql", dsn)
	if err != nil {
		log.Fatalf("Error opening database connection: %v", err)
	}
	defer db.Close()

	_, err = db.Exec(`
		CREATE TABLE IF NOT EXISTS metrics (
			id INT AUTO_INCREMENT,
			metric VARCHAR(255),
			endpoint VARCHAR(255),
			timestamp BIGINT,
			step INT,
			value FLOAT,
			PRIMARY KEY (id)
		)
	`)
	if err != nil {
		log.Fatal(err)
	}
}

func initRedis() {
	rdb = redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
	})
}

func uploadHandler(w http.ResponseWriter, r *http.Request) {
	var metrics []Metric
	if err := json.NewDecoder(r.Body).Decode(&metrics); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	for _, metric := range metrics {
		_, err := db.Exec("INSERT INTO metrics (metric, endpoint, timestamp, step, value) VALUES (?, ?, ?, ?, ?)",
			metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		key := fmt.Sprintf("metric:%s:%s", metric.Endpoint, metric.Metric)
		rdb.LPush(ctx, key, metric)
		rdb.LTrim(ctx, key, 0, 9)
	}

	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"code": 200, "message": "ok", "data": null}`))
}

func queryHandler(w http.ResponseWriter, r *http.Request) {
	endpoint := r.URL.Query().Get("endpoint")
	metric := r.URL.Query().Get("metric")
	startTs := r.URL.Query().Get("start_ts")
	endTs := r.URL.Query().Get("end_ts")

	query := "SELECT metric, timestamp, value FROM metrics WHERE endpoint = ? AND timestamp BETWEEN ? AND ?"
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

	type Data struct {
		Metric string `json:"metric"`
		Values []struct {
			Timestamp int64   `json:"timestamp"`
			Value     float64 `json:"value"`
		} `json:"values"`
	}

	dataMap := make(map[string]*Data)

	for rows.Next() {
		var metricName string
		var timestamp int64
		var value float64

		if err := rows.Scan(&metricName, &timestamp, &value); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		if _, exists := dataMap[metricName]; !exists {
			dataMap[metricName] = &Data{
				Metric: metricName,
			}
		}

		dataMap[metricName].Values = append(dataMap[metricName].Values, struct {
			Timestamp int64   `json:"timestamp"`
			Value     float64 `json:"value"`
		}{
			Timestamp: timestamp,
			Value:     value,
		})
	}

	var responseData []Data
	for _, data := range dataMap {
		responseData = append(responseData, *data)
	}

	response, err := json.Marshal(map[string]interface{}{
		"code":    200,
		"message": "ok",
		"data":    responseData,
	})
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write(response)
}

func main() {
	if err := godotenv.Load(); err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	initDB()
	initRedis()

	r := mux.NewRouter()
	r.HandleFunc("/api/metric/upload", uploadHandler).Methods("POST")
	r.HandleFunc("/api/metric/query", queryHandler).Methods("GET")

	http.Handle("/", r)
	log.Fatal(http.ListenAndServe(":8080", nil))
}
