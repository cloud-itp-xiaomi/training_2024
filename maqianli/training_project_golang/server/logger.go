package server

import (
	"encoding/json"
	"fmt"
	"log"
	"sort"
	"strings"
)

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
		log.Panic("Unsupported logger type")
	}
	return nil
}

type Log struct {
	Meta *LogMeta
	Logs []string
}

type LogMeta struct {
	Hostname string
	File     string
}

func (m *LogMeta) generateGroupKey() string {
	return fmt.Sprintf("%s|%s", m.Hostname, m.File)
}

func (m *LogMeta) String() string {
	return fmt.Sprintf("%s|%s", m.Hostname, m.File)
}

func deserializeLogs(bytes []byte) ([]Log, error) {
	var logs []Log
	err := json.Unmarshal(bytes, &logs)
	return logs, err
}

func serializeLogs(logs []Log) (bytes []byte, err error) {
	return json.Marshal(logs)
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

func groupLogs(logs []Log) []Log {
	key2Meta := make(map[string]*LogMeta)
	key2Logs := make(map[string][]string)
	for _, log := range logs {
		key := log.Meta.generateGroupKey()
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
