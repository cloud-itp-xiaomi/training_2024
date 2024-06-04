// main.go
package main

import (
	"fmt"
    "bytes"
    "encoding/json"
    "net/http"
    "time"
    "log"
)

type Metric struct {
    Metric    string  `json:"metric"`
    Endpoint  string  `json:"endpoint"`
    Timestamp int64   `json:"timestamp"`
    Step      int64   `json:"step"`
    Value     float64 `json:"value"`
}

func collectMetrics() []Metric {
    c, err := getCPUUsage()
    if err != nil {
        log.Fatalf("Error occurred during CPU usage collection. Error: %s", err.Error())
    }
    v, err := getMemoryUsage()
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
            Value:     c,
        },
        {
            Metric:    "mem.used.percent",
            Endpoint:  hostname,
            Timestamp: timestamp,
            Step:      60,
            Value:     v,
        },
    }

    return metrics
}

//未完全实现
func reportMetrics(metrics []Metric) {
    url := ""
    jsonData, err := json.Marshal(metrics)
    if err != nil {
        log.Fatalf("Error occurred during marshaling. Error: %s", err.Error())
    }

    _, err = http.Post(url, "application/json", bytes.NewBuffer(jsonData))
    if err != nil {
        log.Fatalf("Error occurred during reporting metrics. Error: %s", err.Error())
    }
}

func main() {
    for {
        metrics := collectMetrics()
		fmt.Println(metrics)
        //reportMetrics(metrics)
        time.Sleep(60 * time.Second)
    }
}
