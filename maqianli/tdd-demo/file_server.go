package tdd_demo

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type FileServer struct {
	Server
	fileName string
}

func (s *FileServer) saveLog(log Log) error {
	file, err := os.Create(s.fileName)
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
	lines := serializeLog(log)
	for _, line := range lines {
		_, err := writer.WriteString(line + "\n")
		if err != nil {
			return err
		}
	}

	err = writer.Flush()
	if err != nil {
		return err
	}
	return nil
}

func (s *FileServer) readLog() Log {
	file, err := os.Open(s.fileName)
	if err != nil {
		return Log{}
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	if err := scanner.Err(); err != nil {
		return Log{}
	}

	return deserializeLog(lines)
}

func deserializeLog(lines []string) Log {
	var hostname string
	var file string
	var logs []string
	for _, line := range lines {
		parts := strings.Split(line, "|")
		if len(parts) != 3 {
			continue
		}
		hostname = parts[0]
		file = parts[1]
		logs = append(logs, parts[2])

	}
	return Log{
		Hostname: hostname,
		File:     file,
		Logs:     logs,
	}
}

func serializeLog(log Log) []string {
	var lines []string
	for _, line := range log.Logs {
		lines = append(lines, fmt.Sprintf("%s|%s|%s", log.Hostname, log.File, line))
	}
	return lines
}
