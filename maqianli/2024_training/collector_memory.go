package main

import (
	"fmt"
	"strconv"
	"strings"
)

func pickMemValue(text string) (int64, error) {
	parts := strings.Split(text, ":")
	if len(parts) != 2 {
		return 0, fmt.Errorf("invalid format in text: %s", text)
	}
	value := strings.TrimSuffix(parts[1], " kB")
	value = strings.TrimSpace(value)
	return strconv.ParseInt(value, 10, 64)
}

func readMemTotal(filePath string) (int64, error) {
	text, err := readFirstLineFromFile(filePath)
	if err != nil {
		return 0, err
	}
	return pickMemValue(text)
}
