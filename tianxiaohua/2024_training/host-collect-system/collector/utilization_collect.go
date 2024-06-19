package collector

import (
	"fmt"
	"github.com/shirou/gopsutil/cpu"
	"github.com/shirou/gopsutil/mem"
	"os"
	"strconv"
	"time"
)

func getCPUUtilization() float64 {
	percent, err := cpu.Percent(time.Second, false)
	if err != nil {
		fmt.Println("Error getting CPU usage:", err)
		panic(err.Error())
	}
	cpuUsage := fmt.Sprintf("%.2f", percent[0])
	cpuUsageFloat, err := strconv.ParseFloat(cpuUsage, 64)
	if err != nil {
		fmt.Println("Error converting CPU usage to float:", err)
		panic(err.Error())
	}

	fmt.Printf("CPU utilization: %.2f%%\n", cpuUsageFloat)
	return cpuUsageFloat
}

func getMemUtilization() float64 {
	memory, err := mem.VirtualMemory()
	if err != nil {
		fmt.Println("Error getting memory usage:", err)
		panic(err.Error())
	}

	usedPercent := memory.UsedPercent
	memUsedPercentTemp := fmt.Sprintf("%.2f", usedPercent)
	memUsedPercent, err := strconv.ParseFloat(memUsedPercentTemp, 64)
	if err != nil {
		fmt.Println("Error converting Memory usage to float:", err)
		panic(err.Error())
	}
	fmt.Printf("mem utilization: %.2f%%\n", usedPercent)
	return memUsedPercent
}

func getHostname() string {
	hostname, err := os.Hostname()
	if err != nil {
		panic(err.Error())
	}
	return hostname
}
