package tdd_demo

import "testing"
import "github.com/stretchr/testify/assert"

type Log struct {
	Hostname string
	File     string
	Logs     []string
}

type Server struct {
	savedLogs []Log
}

func (s *Server) saveLog(log Log) error {
	return s.saveLogs([]Log{log})
}

func (s *Server) readLog() Log {
	return s.savedLogs[0]
}

func (s *Server) saveLogs(logs []Log) error {
	s.savedLogs = logs
	return nil
}

func (s *Server) readLogs() []Log {
	return s.savedLogs
}

func TestSaveOneLog(t *testing.T) {
	log := Log{}
	server := Server{}
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
	server := Server{}
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
	server := Server{}
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

	server := Server{}
	err := server.saveLogs(logs1)
	assert.Nil(t, err)

	logs2 := server.readLogs()
	assert.Equal(t, logs1, logs2)
}
