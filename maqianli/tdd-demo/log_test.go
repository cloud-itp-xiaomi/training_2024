package tdd_demo

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"testing"
)
import "github.com/stretchr/testify/assert"

type Log struct {
	Hostname string
	File     string
	Logs     []string
}

type MemoryServer struct {
	savedLogs []Log
}

func (s *MemoryServer) saveLog(log Log) error {
	return s.saveLogs([]Log{log})
}

func (s *MemoryServer) readLog() Log {
	return s.savedLogs[0]
}

func (s *MemoryServer) saveLogs(logs []Log) error {
	s.savedLogs = logs
	return nil
}

func (s *MemoryServer) readLogs() []Log {
	return s.savedLogs
}

func TestSaveOneLog(t *testing.T) {
	log := Log{}
	server := MemoryServer{}
	err := server.saveLog(log)
	assert.Nil(t, err)
}

func TestSaveAndReadOneLog(t *testing.T) {
	log1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	server := MemoryServer{}
	err := server.saveLog(log1)
	assert.Nil(t, err)

	log2 := server.readLog()
	assert.Equal(t, log1, log2)
}

func TestSaveAndReadOneLog2(t *testing.T) {
	log1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"abc",
			"123",
		},
	}
	server := MemoryServer{}
	err := server.saveLog(log1)
	assert.Nil(t, err)

	log2 := server.readLog()
	assert.Equal(t, log1, log2)
}

func TestSaveAndReadTwoLogs(t *testing.T) {
	logs1 := []Log{
		{
			Hostname: "my-computer",
			File:     "/home/work/a.log",
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Hostname: "my-computer",
			File:     "/home/work/a.log",
			Logs: []string{
				"abc",
				"123",
			},
		},
	}

	server := MemoryServer{}
	err := server.saveLogs(logs1)
	assert.Nil(t, err)

	logs2 := server.readLogs()
	assert.Equal(t, logs1, logs2)
}

type FileServer struct {
}

func (s FileServer) saveLog(log Log, fileName string) error {
	file, err := os.Create(fileName)
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

func (s FileServer) readLog(fileName string) Log {
	return Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
}

func TestFileServerSaveOneLog(t *testing.T) {
	log := Log{}
	server := FileServer{}
	err := server.saveLog(log, "logs.txt")
	assert.Nil(t, err)
}

func TestFileServerSaveAndReadOneLog(t *testing.T) {
	log1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	server := FileServer{}
	err := server.saveLog(log1, "logs.txt")
	assert.Nil(t, err)

	log2 := server.readLog("logs.txt")
	assert.Equal(t, log1, log2)
}

func TestSerializeLog(t *testing.T) {
	log := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	result := serializeLog(log)
	expected := []string{"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log",
		"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is another log"}
	assert.Equal(t, expected, result)
}

func TestDeserializeLog(t *testing.T) {
	lines := []string{"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log",
		"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is another log"}
	log := deserializeLog(lines)
	expected := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	assert.Equal(t, expected, log)
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
