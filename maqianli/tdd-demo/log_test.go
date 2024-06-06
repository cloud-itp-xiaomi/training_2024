package tdd_demo

import (
	"testing"
)
import "github.com/stretchr/testify/assert"

func TestSaveOneLog(t *testing.T) {
	log := Log{}
	server := getServer(serverType)
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
	server := getServer(serverType)
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

func TestFileServerSaveOneLog(t *testing.T) {
	log := Log{}
	server := FileServer{
		fileName: "logs.txt",
	}
	err := server.saveLog(log)
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
	server := FileServer{
		fileName: "logs.txt",
	}
	err := server.saveLog(log1)
	assert.Nil(t, err)

	log2 := server.readLog()
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

var serverType = 1

func TestSaveAndReadOneLogWithDifferentServer(t *testing.T) {
	log1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	server := getServer(serverType)
	err := server.saveLog(log1)
	assert.Nil(t, err)

	log2 := server.readLog()
	assert.Equal(t, log1, log2)
}

func getServer(serverType int) Server {
	switch serverType {
	case 0:
		return &MemoryServer{}
	case 1:
		return &FileServer{
			fileName: "logs.txt",
		}
	default:
		return nil
	}
}
