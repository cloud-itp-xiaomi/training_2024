package collector

import (
	"fmt"
	"strconv"
	"strings"
)

// collectCPUUsageFromFile  reads the first line of a file and returns the CPU usage percentage
func collectCPUUsageFromFile(filePath string) (float64, error) {
	texts, err := readLinesFromFile(filePath, 1)
	if err != nil {
		return 0, err
	}
	data, err := parseCPUUsageText(texts[0])
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
