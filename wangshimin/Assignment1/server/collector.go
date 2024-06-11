// collector.go
package main

import (
	"fmt"
    "bytes"
    "encoding/json"
    "net/http"
    "time"
    "log"
    "io"
)



func collectMetrics() []Metric {
    cpuUsage, err := getCPUUsage()
    if err != nil {
        log.Fatalf("Error occurred during CPU usage collection. Error: %s", err.Error())
    }
    memoryUsage, err := getMemoryUsage()
    if err != nil {
        log.Fatalf("Error occurred during memory usage collection. Error: %s", err.Error())
    }

    hostname := "192.168.80.192"
    timestamp := time.Now().Unix()

    metrics := []Metric{
        {
            Metric:    "cpu.used.percent",
            Endpoint:  hostname,
            Timestamp: timestamp,
            Step:      60,
            Value:     cpuUsage,
        },
        {
            Metric:    "mem.used.percent",
            Endpoint:  hostname,
            Timestamp: timestamp,
            Step:      60,
            Value:     memoryUsage,
        },
    }

    return metrics
}

func reportMetrics(metrics []Metric) {
    url := "http://localhost:8080/api/metric/upload"
    jsonData, err := json.Marshal(metrics)
    if err != nil {
        log.Fatalf("Error occurred during marshaling. Error: %s", err.Error())
    }

    fmt.Printf("Sending JSON data: %s\n", jsonData)

    resp, err := http.Post(url, "application/json", bytes.NewBuffer(jsonData))
    if err != nil {
        log.Fatalf("Error occurred during reporting metrics. Error: %s", err.Error())
    }
    defer resp.Body.Close()

    // 检查响应状态码
    if resp.StatusCode == http.StatusOK {
        fmt.Println("Metrics successfully reported.")
    } else {
        fmt.Printf("Server returned non-OK status: %d\n", resp.StatusCode)
        body, err := io.ReadAll(resp.Body)
        if err != nil {
            fmt.Printf("Response body: %s\n", body)
        }
    }
}