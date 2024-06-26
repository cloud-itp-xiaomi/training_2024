package main

import (
	"bufio"
	"bytes"
	"collector/collectorDataType"
	"encoding/json"
	"fmt"
	"github.com/shirou/gopsutil/v3/cpu"
	"io"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"
)

func main() {
	fmt.Println("Starting collector...")

	for {
		data := collectData()
		sendData(data)
		time.Sleep(60 * time.Second)
	}
}

func getCpuUsage() (float64, error) {
	percentages, err := cpu.Percent(1*time.Second, false)
	if err != nil {
		return 0, err
	}

	// 计算所有核心的平均使用率
	total := 0.0
	for _, percentage := range percentages {
		total += percentage
	}
	average := total / float64(len(percentages))

	return average, nil
}

func getMemoryUsage() (float64, error) {
	file, err := os.Open("/proc/meminfo")
	if err != nil {
		return 0, err
	}
	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			log.Println("Error close memory file:", err)
		}
	}(file)

	var totalMem, freeMem uint64
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		fields := strings.Fields(line)
		if len(fields) < 2 {
			continue
		}

		key := fields[0][:len(fields[0])-1]
		value, err := strconv.ParseUint(fields[1], 10, 64)
		if err != nil {
			return 0, err
		}

		switch key {
		case "MemTotal":
			totalMem = value
		case "MemFree", "Buffers", "Cached", "SReclaimable":
			freeMem += value
		}
	}

	if err := scanner.Err(); err != nil {
		return 0, err
	}

	usedMem := totalMem - freeMem
	memoryUsage := (float64(usedMem) / float64(totalMem)) * 100.0

	return memoryUsage, nil
}

func getHostName() string {
	hostname, err := os.Hostname()
	if err != nil {
		log.Println("Error getting hostname:", err)
		return "unknown"
	}
	return hostname
}

func sendData(data []collectorDataType.MetricData) {
	jsonData, err := json.Marshal(data)
	if err != nil {
		log.Println("Error marshalling data:", err)
		return
	}

	resp, err := http.Post("http://server:8080/api/metric/upload", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		//log.Println("Error sending data:", err)
		return
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			log.Println("Error close send body:", err)
		}
	}(resp.Body)

	if resp.StatusCode != http.StatusOK {
		log.Println("Received non-OK response:", resp.StatusCode)
	}
}

func collectData() []collectorDataType.MetricData {
	hostname := getHostName()

	cpuUsage, err := getCpuUsage()
	if err != nil {
		log.Fatal(err)
	}
	
	memoryUsage, err := getMemoryUsage()
	if err != nil {
		log.Fatal(err)
	}

	timestamp := time.Now().Unix()

	return []collectorDataType.MetricData{
		{
			Metric:    "cpu.used.percent",
			Endpoint:  hostname,
			Timestamp: timestamp,
			Step:      60,
			Value:     (cpuUsage*1e2 + 0.5) * 1e-2,
		},
		{
			Metric:    "mem.used.percent",
			Endpoint:  hostname,
			Timestamp: timestamp,
			Step:      60,
			Value:     (memoryUsage*1e2 + 0.5) * 1e-2,
		},
	}

}
