package main

import (
	"bufio"
	"fmt"
	"os"
)

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
