package collector

import (
	"bufio"
	"fmt"
	"os"
)

func readFirstLineFromFile(filePath string) (string, error) {
	texts, err := readLinesFromFile(filePath, 1)
	if len(texts) > 0 {
		return texts[0], err
	}

	return "", err
}

func readLinesFromFile(filePath string, count int) ([]string, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("error opening file: %v", err)
	}
	defer file.Close()

	lines := make([]string, 0)
	scanner := bufio.NewScanner(file)
	for len(lines) < count && scanner.Scan() {
		if err = scanner.Err(); err != nil {
			return nil, fmt.Errorf("error reading file: %v", err)
		}
		lines = append(lines, scanner.Text())
	}
	if len(lines) < count {
		return nil, fmt.Errorf("not enough lines in file, expected %d but got %d", count, len(lines))
	}
	return lines, nil
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
