package server

import (
	"bufio"
	"errors"
	"fmt"
	"os"
	"strings"
)

type FileLogger struct {
}

func (l *FileLogger) saveLogs(logs []Log) error {
	lines, err := l.serializeMultiLogs(logs)
	if err != nil {
		return err
	}

	file, err := os.Create("training_logs.txt")
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
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

func (l *FileLogger) readLogs() ([]Log, error) {
	file, err := os.Open("training_logs.txt")
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	if err := scanner.Err(); err != nil {
		return nil, err
	}

	return l.deserializeMultiLogs(lines)
}

func (l *FileLogger) serializeLog(log Log) ([]string, error) {
	var logs []string
	for _, line := range log.Logs {
		logs = append(logs, fmt.Sprintf("%s|%s|%s", log.Meta.Hostname, log.Meta.File, line))
	}
	return logs, nil
}

func (l *FileLogger) deserializeLog(log string) (Log, error) {
	parts := strings.Split(log, "|")
	if len(parts) != 3 {
		return Log{}, errors.New("invalid log format")
	}
	hostname := parts[0]
	file := parts[1]
	logs := parts[2:]
	return Log{
		Meta: &LogMeta{
			Hostname: hostname,
			File:     file,
		}, Logs: logs}, nil
}

func (l *FileLogger) serializeMultiLogs(logs []Log) ([]string, error) {
	var texts []string
	for _, log := range logs {
		if text, err := l.serializeLog(log); err != nil {
			return nil, err
		} else {
			texts = append(texts, text...)
		}

	}
	return texts, nil
}

func (l *FileLogger) deserializeMultiLogs(texts []string) ([]Log, error) {
	var logs []Log
	for _, text := range texts {
		log, err := l.deserializeLog(text)
		if err != nil {
			return nil, err
		}
		logs = append(logs, log)
	}

	logs = groupLogs(logs)
	return logs, nil
}
