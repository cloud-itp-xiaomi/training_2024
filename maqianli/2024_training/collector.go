package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

// collectCPUUsageFromFile  reads the first line of a file and returns the CPU usage percentage
func collectCPUUsageFromFile(filePath string) (float64, error) {
	text, err := readFirstLineFromFile(filePath)
	if err != nil {
		return 0, err
	}
	data, err := parseCPUUsageText(text)
	if err != nil {
		return 0, err
	}
	idle, err := collectIdleCPUTime(data)
	if err != nil {
		return 0, err
	}
	total, err := collectTotalCPUTime(data)
	if err != nil {
		return 0, err
	}
	idlePercentage, err := calculateCPUIdlePercentage(idle, total)
	if err != nil {
		return 0, err
	}

	return formatFloat(1-idlePercentage, 6), nil
}

func collectTotalCPUTime(data []int) (int, error) {
	total := 0
	for _, num := range data {
		total += num
	}

	return total, nil
}

func readFirstLineFromFile(filePath string) (string, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return "", fmt.Errorf("error opening file: %v", err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	if scanner.Scan() {
		return scanner.Text(), nil
	}

	if err = scanner.Err(); err != nil {
		return "", fmt.Errorf("error reading file: %v", err)
	}

	return "", nil
}

func parseCPUUsageText(text string) ([]int, error) {
	arr := strings.Fields(text)
	if len(arr) != 11 {
		return nil, fmt.Errorf("expected 11 fields, got %d", len(arr))
	}
	arr = arr[1:]
	var data []int
	for _, s := range arr {
		n, err := strconv.Atoi(s)
		if err != nil {
			return nil, fmt.Errorf("failed to parse %q as integer", s)
		}
		data = append(data, n)
	}
	return data, nil
}

func calculateCPUIdlePercentage(idle int, total int) (float64, error) {
	if total <= 0 {
		return 0, fmt.Errorf("total time should be greater than 0")
	}
	return formatFloat(float64(idle)/float64(total), 6), nil
}

func collectIdleCPUTime(data []int) (int, error) {
	return data[3], nil
}

func formatFloat(num float64, decimalPlaces int) float64 {
	formatString := fmt.Sprintf("%%.%df", decimalPlaces)
	formattedNum := fmt.Sprintf(formatString, num)

	var result float64
	_, err := fmt.Sscan(formattedNum, &result)
	if err != nil {
		fmt.Println("Error parsing formatted number:", err)
		return 0.0
	}

	return result
}
