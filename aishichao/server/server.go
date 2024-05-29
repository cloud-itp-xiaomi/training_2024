package main

import (
    "encoding/json"
    "fmt"
    "log"
    "net/http"
    //"sync"
    "time"
    //"strconv"
    "context"
    "github.com/redis/go-redis/v9"
)

type MetricData struct{
     Metric      string    `json:"metric"`
     Endpoint    string    `json:"endpoint"`
     Timestamp   int64     `json:"timestamp"`
     Step        int64     `json:"step"`
     Value       float64   `json:"value"`
}

var ctx=context.Background()
var rdb *redis.Client
var (
    metrics []MetricData
)

func main() {
    fmt.Println("Starting server...")
    
    rdb = redis.NewClient(&redis.Options{
        Addr:     "redis:6379",
        Password: "",
        DB:       0,
    })

    http.HandleFunc("/api/metric/upload", uploadHandler)
    http.HandleFunc("/api/metric/query", queryHandler)
    
    go runTest()

    log.Fatal(http.ListenAndServe(":8080", nil))
}

// 上报接口
func uploadHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
        return
    }

    var data []MetricData
    if err := json.NewDecoder(r.Body).Decode(&data); err != nil {
        http.Error(w, "Invalid request body", http.StatusBadRequest)
        return
    }

    for _,metric:= range data{
        key:="metrics:"+metric.Metric
        jsonData,err:=json.Marshal(metric)
        if err!=nil{
            log.Println(err)
            continue
        }
        rdb.LPush(ctx,key,jsonData)
        rdb.LTrim(ctx,key,0,9)
        log.Println("metric:",metric.Metric,",endpoint:",metric.Endpoint,",timestamp:",metric.Timestamp,",step:",metric.Step,",value:",metric.Value)

    w.WriteHeader(http.StatusOK)
    }
}

// 查询接口
func queryHandler(w http.ResponseWriter, r *http.Request) {
    metric:=r.URL.Query().Get("metric")
    if metric ==""{
        http.Error(w, "query error!",http.StatusBadRequest)
        return
    }
    
    key:="metrics:"+metric
    result,err:=rdb.LRange(ctx,key,0,-1).Result()
    if err!=nil{
        http.Error(w, "redis query error!",http.StatusInternalServerError)
        return
    }
    
    var metrics []MetricData
    for _,item:=range result{
        var metricData MetricData
        if err := json.Unmarshal([]byte(item), &metricData); err != nil {
           log.Printf("Error unmarshalling metric data: %v", err)
           continue
        }
        metrics = append(metrics, metricData)
    }

    w.Header().Set("Content-Type", "application/json")
    if err := json.NewEncoder(w).Encode(metrics); err != nil {
        http.Error(w, "Error encoding response", http.StatusInternalServerError)
    }
    
}

// 测试函数，验证接收到了数据
func runTest() {
    time.Sleep(5 * time.Second) // 等待 5 秒，以确保有时间接收数据

    if len(metrics) > 0 {
       fmt.Println("Test Passed: Data received successfully.")
       fmt.Printf("First data point: %+v\n", metrics[0])
    } else {
       fmt.Println("Test Failed: No data received.")
    }
}

func testHandler(w http.ResponseWriter, r *http.Request) {
    metrics := []MetricData{
       {
           Metric: "cpu.used.percent",
           Endpoint: "test-endpoint",
           Timestamp: time.Now().Unix(),
           Step: 60,
           Value: 50.0,
       },
       {
           Metric: "mem.used.percent",
           Endpoint: "test-endpoint",
           Timestamp: time.Now().Unix(),
           Step: 60,
           Value: 30.0,
       },
    }

    w.Header().Set("Content-Type", "application/json")
    if err := json.NewEncoder(w).Encode(metrics); err != nil {
       http.Error(w, "Error encoding response", http.StatusInternalServerError)
    }
}


