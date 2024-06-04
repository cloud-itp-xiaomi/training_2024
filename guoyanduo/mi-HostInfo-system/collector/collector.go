package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/shirou/gopsutil/v3/cpu"
	"github.com/shirou/gopsutil/v3/mem"
)

type Metric struct {
	Metric    string            `json:"metric"`
	Endpoint  string            `json:"endpoint"`
	Timestamp int64             `json:"timestamp"`
	Step      int64             `json:"step"`
	Value     float64           `json:"value"`
	Tags      map[string]string `json:"tags,omitempty"`
}

func collectAndSend() {
	for {
		cpuUsage, _ := cpu.Percent(0, false)
		memUsage, _ := mem.VirtualMemory()

		timestamp := time.Now().Unix()

		metrics := []Metric{
			{
				Metric:    "cpu.used.percent",
				Endpoint:  "my-computer",
				Timestamp: timestamp,
				Step:      60,
				Value:     cpuUsage[0],
			},
			{
				Metric:    "mem.used.percent",
				Endpoint:  "my-computer",
				Timestamp: timestamp,
				Step:      60,
				Value:     memUsage.UsedPercent,
			},
		}

		data, _ := json.Marshal(metrics)
		resp, err := http.Post("http://localhost:8080/api/metric/upload", "application/json", bytes.NewBuffer(data))
		if err != nil {
			fmt.Println("Error sending data:", err)
		} else {
			resp.Body.Close()
		}

		time.Sleep(60 * time.Second)
	}
}

func main() {
	collectAndSend()
}
