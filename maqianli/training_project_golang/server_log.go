package main

import (
	"bufio"
	"encoding/json"
	"errors"
	"fmt"
	"os"
	"sort"
	"strings"
)

type Log struct {
	Meta *LogMeta
	Logs []string
}

const (
	FileLoggerType = iota
	MemLoggerType
)

type Logger interface {
	saveLogs(logs []Log) error
	readLogs() ([]Log, error)
	serializeLog(log Log) ([]string, error)
	deserializeLog(log string) (Log, error)
	serializeMultiLogs(logs []Log) ([]string, error)
	deserializeMultiLogs(logs []string) ([]Log, error)
}

func getLogger(logger int) Logger {
	switch logger {
	case FileLoggerType:
		return &FileLogger{}
	case MemLoggerType:
		return &MemLogger{}
	default:
		panic("Unsupported logger type")
	}
}

func deserializeLogs(bytes []byte) ([]Log, error) {
	var logs []Log
	err := json.Unmarshal(bytes, &logs)
	return logs, err
}

func serializeLogs(logs []Log) (bytes []byte, err error) {
	return json.Marshal(logs)
}

type MemLogger struct {
	savedLogs []Log
}

func (l *MemLogger) deserializeMultiLogs(logs []string) ([]Log, error) {
	panic("implement me")
}

func (l *MemLogger) serializeMultiLogs(logs []Log) ([]string, error) {
	panic("implement me")
}

func (l *MemLogger) deserializeLog(log string) (Log, error) {
	panic("implement me")
}

func (l *MemLogger) serializeLog(log Log) ([]string, error) {
	panic("implement me")
}

func (l *MemLogger) readLogs() ([]Log, error) {
	return l.savedLogs, nil
}

func (l *MemLogger) saveLogs(logs []Log) error {
	l.savedLogs = logs
	return nil
}

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

type LogMeta struct {
	Hostname string
	File     string
}

func (m *LogMeta) String() string {
	return fmt.Sprintf("%s|%s", m.Hostname, m.File)
}

func parseGroupKey(key string) *LogMeta {
	parts := strings.Split(key, "|")
	if len(parts) == 2 {
		return &LogMeta{
			Hostname: parts[0],
			File:     parts[1],
		}
	}
	return nil
}

func generateGroupKey(meta *LogMeta) string {
	return fmt.Sprintf("%s|%s", meta.Hostname, meta.File)
}

func groupLogs(logs []Log) []Log {
	key2Meta := make(map[string]*LogMeta)
	key2Logs := make(map[string][]string)
	for _, log := range logs {
		key := generateGroupKey(log.Meta)
		if _, ok := key2Meta[key]; !ok {
			key2Meta[key] = log.Meta
			key2Logs[key] = []string{}
		}
		key2Logs[key] = append(key2Logs[key], log.Logs...)
	}

	result := make([]Log, 0, len(key2Meta))
	for key, logs := range key2Logs {
		result = append(result, Log{
			Meta: key2Meta[key],
			Logs: logs,
		})
	}
	sort.Slice(result, func(i, j int) bool {
		return result[i].Meta.String() < result[j].Meta.String()
	})
	return result
}
