package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

type Metric struct {
	Metric string  `json:"metric"`
	Values []Value `json:"values"`
}

type Value struct {
	Timestamp int64   `json:"timestamp"`
	Value     float64 `json:"value"`
}

func queryMetrics(metric string) ([]Metric, error) {
	resp, err := http.Get("http://localhost:8080/api/metric/query?metric=" + metric)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("HTTP request failed with status code %d", resp.StatusCode)
	}

	var metrics []Metric
	err = json.NewDecoder(resp.Body).Decode(&metrics)
	if err != nil {
		return nil, err
	}

	return metrics, nil
}

func main() {
	cpuMetrics, err := queryMetrics("cpu.used.percent")
	if err != nil {
		log.Fatalf("Error querying metrics for cpu.used.percent: %v", err)
	}

	memMetrics, err := queryMetrics("mem.used.percent")
	if err != nil {
		log.Fatalf("Error querying metrics for mem.used.percent: %v", err)
	}

	fmt.Println("CPU Metrics:")
	for _, metric := range cpuMetrics {
		fmt.Printf("Metric: %s\n", metric.Metric)
		for _, value := range metric.Values {
			fmt.Printf("Timestamp: %d, Value: %.2f\n", value.Timestamp, value.Value)
		}
	}

	fmt.Println("\nMemory Metrics:")
	for _, metric := range memMetrics {
		fmt.Printf("Metric: %s\n", metric.Metric)
		for _, value := range metric.Values {
			fmt.Printf("Timestamp: %d, Value: %.2f\n", value.Timestamp, value.Value)
		}
	}
}

