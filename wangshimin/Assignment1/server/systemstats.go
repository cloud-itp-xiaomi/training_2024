// systemstats.go
package main

import (
    "fmt"
    "io/ioutil"
    "strconv"
    "strings"
)

// 获取CPU使用率的函数
func getCPUUsage() (float64, error) {
    contents, err := ioutil.ReadFile("/proc/stat")
    if err != nil {
        return 0, err
    }
    lines := strings.Split(string(contents), "\n")
    for _, line := range lines {
        fields := strings.Fields(line)
        if fields[0] == "cpu" {
            total := 0.0
            for i := 1; i < len(fields); i++ {
                val, err := strconv.ParseFloat(fields[i], 64)
                if err != nil {
                    return 0, err
                }
                total += val
            }
            idle, _ := strconv.ParseFloat(fields[4], 64)
            return 100 * (1 - idle/total), nil
        }
    }
    return 0, nil
}

// 获取内存使用率的函数
func getMemoryUsage() (float64, error) {
    contents, err := ioutil.ReadFile("/proc/meminfo")
    if err != nil {
        return 0, err
    }
    lines := strings.Split(string(contents), "\n")

    var total, available float64
    foundTotal, foundAvailable := false, false

    for _, line := range lines {
        fields := strings.Fields(line)
        if len(fields) < 2 {
            continue
        }
        if fields[0] == "MemTotal:" {
            total, err = strconv.ParseFloat(fields[1], 64)
            if err != nil {
                return 0, err
            }
            foundTotal = true
        } else if fields[0] == "MemAvailable:" {
            available, err = strconv.ParseFloat(fields[1], 64)
            if err != nil {
                return 0, err
            }
            foundAvailable = true
        }
    }

    if !foundTotal || !foundAvailable {
        return 0, fmt.Errorf("could not find necessary fields in /proc/meminfo")
    }

    return 100 * (1 - available/total), nil
}