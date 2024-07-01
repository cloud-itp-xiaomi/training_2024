// collector.go
package main

import (
	"fmt"
    "bytes"
    "encoding/json"
    "net/http"
    "time"
    "io"
)



func collectMetrics() ([]Metric,error) {
    cpuUsage, err := getCPUUsage()
    if err != nil {
        return nil, fmt.Errorf("error occurred during CPU usage collection: %w", err)
    }
    memoryUsage, err := getMemoryUsage()
    if err != nil {
        return nil, fmt.Errorf("error occurred during memory usage collection: %w", err)
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

    return metrics,nil
}

func reportMetrics(metrics []Metric) error{
    url := "http://localhost:8080/api/metric/upload"
    jsonData, err := json.Marshal(metrics)
    if err != nil {
        return fmt.Errorf("error occurred during marshaling: %w", err)
    }

    fmt.Printf("Sending JSON data: %s\n", jsonData)

    httpClient := &http.Client{
        Timeout: 10 * time.Second, 
    }

    resp, err := httpClient.Post(url, "application/json", bytes.NewBuffer(jsonData))
    if err != nil {
        return fmt.Errorf("error occurred during reporting metrics: %w", err)
    }
    defer resp.Body.Close()

    // 检查响应状态码
    if resp.StatusCode == http.StatusOK {
		fmt.Println("Metrics successfully reported.")
	} else {
		body, err := io.ReadAll(resp.Body)
		if err != nil {
			return fmt.Errorf("server returned non-OK status: %d, and error reading response body: %w", resp.StatusCode, err)
		}
		return fmt.Errorf("server returned non-OK status: %d, response body: %s", resp.StatusCode, string(body))
	}
    return nil
}