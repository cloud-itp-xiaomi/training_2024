package collector

import (
	"bytes"
	"collector/model"
	"collector/tools"
	"fmt"
	"os/exec"
	"strconv"
	"strings"
	"sync"
	"time"
)

var urlUpload = "http://localhost:8080/api/metric/upload"

// SendMetricData 上报数据
func SendMetricData() {
	cpuUsage := getCpuUsage()
	memUsage := getMemoryUsage()

	cpuMetric := model.Metric{
		Metric:    "cpu.used.percent",
		Endpoint:  "my-computer",
		Timestamp: time.Now().Unix(),
		Step:      60,
		Value:     cpuUsage,
	}

	memMetric := model.Metric{
		Metric:    "mem.used.percent",
		Endpoint:  "my-computer",
		Timestamp: time.Now().Unix(),
		Step:      60,
		Value:     memUsage,
	}

	var wg sync.WaitGroup
	wg.Add(2)

	go func() {
		defer wg.Done()
		sendMetricPost(cpuMetric)
	}()

	go func() {
		defer wg.Done()
		sendMetricPost(memMetric)
	}()

	// 等待所有 Goroutine 完成
	wg.Wait()
}

// 获取CPU使用率
func getCpuUsage() float64 {
	cmdCPU := exec.Command("wmic", "cpu", "get", "loadpercentage")
	var outCPU bytes.Buffer
	cmdCPU.Stdout = &outCPU
	err := cmdCPU.Run()
	if err != nil {
		fmt.Println("执行 wmic 命令获取 CPU 使用率失败: ", err)
		return 0.0
	}

	// 清洗并解析CPU使用率
	cpuLines := strings.Split(outCPU.String(), "\n")
	if len(cpuLines) < 2 {
		// 应该是两行
		fmt.Println("解析 CPU 使用率失败")
		return 0.0
	}
	cpuUsage, err := strconv.Atoi(strings.TrimSpace(cpuLines[1])) // 第二行是CPU使用率的具体数值
	if err != nil {
		fmt.Println("转换 CPU 使用率为整数失败: ", err)
		return 0.0
	}

	return float64(cpuUsage)
}

// 获取内存使用率
func getMemoryUsage() float64 {
	// 获取内存使用率
	cmdMem := exec.Command("wmic", "os", "get", "FreePhysicalMemory,TotalVisibleMemorySize")
	var outMem bytes.Buffer
	cmdMem.Stdout = &outMem
	err := cmdMem.Run()
	if err != nil {
		fmt.Println("执行 wmic 命令获取内存使用率失败: ", err)
		return 0.0
	}

	// 解析内存使用率
	lines := strings.Split(outMem.String(), "\n")
	var freeMem, totalMem int
	for _, line := range lines {
		line = strings.TrimSpace(line)
		if line == "" || strings.Contains(line, "FreePhysicalMemory") {
			// 跳过第一行或空行
			continue
		}
		parts := strings.Fields(line)
		if len(parts) == 2 {
			freeMem, err = strconv.Atoi(parts[0])
			if err != nil {
				fmt.Println("转换 FreePhysicalMemory 为整数失败: ", err)
				return 0.0
			}
			totalMem, err = strconv.Atoi(parts[1])
			if err != nil {
				fmt.Println("转换 TotalVisibleMemorySize 为整数失败: ", err)
				return 0.0
			}
			break
		}
	}
	if totalMem == 0 {
		fmt.Println("总内存不能为零")
		return 0.0
	}
	memUsage := 100 * (1 - float64(freeMem)/float64(totalMem))
	return memUsage
}

// 调用POST请求发送数据
func sendMetricPost(m model.Metric) {
	client := tools.NewHTTPClient()
	_, err := client.PostJSON(urlUpload, m)
	if err != nil {
		fmt.Printf("发送 %v 数据失败: %v\n", m, err)
	} else {
		fmt.Printf("发送 %v 数据成功\n", m)
	}
}
