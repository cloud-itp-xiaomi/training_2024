package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"
	//"time"
	// "github.com/gin-gonic/gin"
)

type JsonStruct struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int64   `json:"step"`
	Value     float64 `json:"value"`
}

func main() {
	for {
		hostname, err := os.Hostname()
		if err != nil {
			return
		}
		timestamp := time.Now().Unix()
		cpuUsage := CpuUsage()
		memoryUsage := MemoryUsage()
		data := []JsonStruct{
			{
				Metric:    "cpu.used.percent",
				Endpoint:  hostname,
				Timestamp: timestamp,
				Step:      60,
				Value:     cpuUsage,
			},
			{
				Metric:    "memory.used.percent",
				Endpoint:  hostname,
				Timestamp: timestamp,
				Step:      60,
				Value:     memoryUsage,
			},
		}
		jsonData, err := json.Marshal(data)
		if err != nil {
			return
		}
		resp, err := http.Post("http://localhost:8080/api/metric/upload", "application/json", bytes.NewBuffer(jsonData))
		if err != nil {
			log.Println("Post err: ", err)
			continue
		}
		resp.Body.Close()
		fmt.Println("Response: ", resp.Status)
		time.Sleep(time.Minute)
	}
}

func CpuUsage() float64 {
	// fmt.Println("CPU usage % at 1 second intervals:\n")
	var prevIdleTime, prevTotalTime uint64
	file, err := os.Open("/proc/stat")
	if err != nil {
		log.Fatal(err)
	}
	scanner := bufio.NewScanner(file)
	scanner.Scan()
	firstLine := scanner.Text()[5:] // get rid of cpu plus 2 spaces
	file.Close()
	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
	split := strings.Fields(firstLine)
	idleTime, _ := strconv.ParseUint(split[3], 10, 64)
	totalTime := uint64(0)
	for _, s := range split {
		u, _ := strconv.ParseUint(s, 10, 64)
		totalTime += u
	}

	deltaIdleTime := idleTime - prevIdleTime
	deltaTotalTime := totalTime - prevTotalTime
	cpuUsage := (1.0 - float64(deltaIdleTime)/float64(deltaTotalTime)) * 100.0

	prevIdleTime = idleTime
	prevTotalTime = totalTime
	return cpuUsage
}

func MemoryUsage() float64 {
	// fmt.Println("Memory usage :\n")
	file, err := os.Open("/proc/meminfo")
	if err != nil {
		return 0
	}
	var totalMem, freeMem uint64
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		fields := strings.Fields(line)
		if len(fields) < 2 {
			continue
		}
		key := fields[0]
		// fmt.Println(key)
		value, err := strconv.ParseUint(fields[1], 10, 64)
		if err != nil {
			log.Fatalf("Failed to parse %s: %v", fields[1], err)
			return 0
		}
		switch key {
		case "MemTotal:":
			totalMem = value
		case "MemFree:", "Buffers:", "Cached:", "SReclaimable:":
			freeMem += value
		}
	}
	if err := scanner.Err(); err != nil {
		log.Fatalf("Failed to read /proc/meminfo: %v", err)
	}

	usedMem := totalMem - freeMem
	// fmt.Println("totalMem :%f", totalMem)
	// fmt.Println("freeMem :%f", freeMem)
	memoryUsage := (float64(usedMem) / float64(totalMem)) * 100.0
	// fmt.Println("Memory usage :%f", memoryUsage)
	file.Close()
	return memoryUsage
}
