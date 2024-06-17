package collector

import (
	"fmt"
	"log"
	"strconv"
	"strings"
)

type MemStat struct {
	MemTotal     int64
	MemFree      int64
	MemAvailable int64
}

func (s *MemStat) UsagePercentage() float64 {
	v := float64(s.MemTotal-s.MemFree) / float64(s.MemTotal)
	return formatFloat(v, 6)
}

func readMemStat(filePath string) (*MemStat, error) {
	const lineCount = 3
	texts, err := readLinesFromFile(filePath, lineCount)
	if err != nil {
		return nil, err
	}
	if len(texts) != lineCount {
		return nil, fmt.Errorf("invalid number of lines in file: %s", filePath)
	}

	return &MemStat{
		MemTotal:     pickMemValue(texts[0]),
		MemFree:      pickMemValue(texts[1]),
		MemAvailable: pickMemValue(texts[2]),
	}, nil
}

func pickMemValue(text string) int64 {
	parts := strings.Split(text, ":")
	if len(parts) != 2 {
		log.Printf("Invalid format in text: %s\n", text)
		return 0
	}
	str := strings.TrimSpace(strings.TrimSuffix(parts[1], " kB"))
	v, err := strconv.ParseInt(str, 10, 64)
	if err != nil {
		log.Printf("Failed to parse value: %s, error: %v", str, err)
		return 0
	}
	return v
}
