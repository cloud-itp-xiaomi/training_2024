package main

import (
    "encoding/json"
    "fmt"
    "net/http"
    "net/url"
    "os"
)

type Metric struct {
    Metric    string  `json:"metric"`
    Endpoint  string  `json:"endpoint"`
    Timestamp int64   `json:"timestamp"`
    Step      int64   `json:"step"`
    Value     float64 `json:"value"`
}

func main() {
    // 定义查询参数
    endpoint := "192.168.80.192"
    metric := ""
    startTs := "0"
    endTs := fmt.Sprintf("%d", int64(^uint64(0)>>1))

    // 构造查询URL
    query := url.Values{}
    query.Set("endpoint", endpoint)
    query.Set("metric", metric)
    query.Set("start_ts", startTs)
    query.Set("end_ts", endTs)

    // 发送查询请求
    resp, err := http.Get("http://localhost:8080/api/metric/query?" + query.Encode())
    if err != nil {
        fmt.Printf("Error making request: %s\n", err.Error())
        os.Exit(1)
    }
    defer resp.Body.Close()

    if resp.StatusCode != http.StatusOK {
        fmt.Printf("Server returned non-OK status: %d\n", resp.StatusCode)
        os.Exit(1)
    }

    var metrics []Metric
    if err := json.NewDecoder(resp.Body).Decode(&metrics); err != nil {
        fmt.Printf("Error decoding response: %s\n", err.Error())
        os.Exit(1)
    }

    fmt.Printf("查询到的度量指标: %v\n", metrics)
}