package server

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestVerifyLogStructure(t *testing.T) {
	log := Log{
		Meta: &LogMeta{
			Hostname: "my-computer",
			File:     "/home/work/a.log",
		},
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	assert.Equal(t, 2, len(log.Logs))
}

func TestSerializeLogs(t *testing.T) {
	logs := []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}
	bytes, err := serializeLogs(logs)
	assert.Nil(t, err)
	assert.NotEmpty(t, bytes)
	logs2, err := deserializeLogs(bytes)
	assert.Nil(t, err)
	assert.Equal(t, len(logs), len(logs2))
	assert.Equal(t, logs[0], logs2[0])
	assert.Equal(t, logs[1], logs2[1])
}

func TestWriteAndReadLogsWithMemLogger(t *testing.T) {
	logs := []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}
	logger := getLogger(MemLoggerType)
	err := logger.saveLogs(logs)
	assert.Nil(t, err)
	logs2, err := logger.readLogs()
	assert.Nil(t, err)
	assert.Equal(t, len(logs), len(logs2))
	assert.Equal(t, logs[0], logs2[0])
	assert.Equal(t, logs[1], logs2[1])
}

func TestWriteAndReadLogsWithFileLogger(t *testing.T) {
	logs := []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}

	logger := getLogger(FileLoggerType)
	err := logger.saveLogs(logs)
	assert.Nil(t, err)
	logs2, err := logger.readLogs()
	assert.Nil(t, err)
	assert.Equal(t, logs, logs2)
}

func TestFileLoggerSerializeLog(t *testing.T) {
	log := Log{
		Meta: &LogMeta{
			Hostname: "my-computer",
			File:     "/home/work/a.log",
		},
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
	}
	logger := getLogger(FileLoggerType)
	serializedLogs, err := logger.serializeLog(log)
	assert.Nil(t, err)
	assert.Equal(t, "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log", serializedLogs[0])
	assert.Equal(t, "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is another log", serializedLogs[1])
	assert.Equal(t, 2, len(serializedLogs))
}

func TestFileLoggerDeserializeLog(t *testing.T) {
	serializedLog := "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log"
	logger := getLogger(FileLoggerType)
	log, err := logger.deserializeLog(serializedLog)
	assert.Nil(t, err)
	assert.Equal(t, "my-computer", log.Meta.Hostname)
	assert.Equal(t, "/home/work/a.log", log.Meta.File)
	assert.Equal(t, "2024-05-16 10:11:51 +08:00 This is a log", log.Logs[0])
	assert.Equal(t, 1, len(log.Logs))
}

func TestFileLoggerDeserializeLogFailed(t *testing.T) {
	serializedLog := "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log|unexpected format"
	logger := getLogger(FileLoggerType)
	log, err := logger.deserializeLog(serializedLog)
	assert.NotNil(t, err)
	assert.Nil(t, log.Logs)
}

func TestFileLoggerSerializeMultipleLogs(t *testing.T) {
	logs := []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}
	logger := getLogger(FileLoggerType)
	serializedLogs, err := logger.serializeMultiLogs(logs)
	assert.Nil(t, err)
	assert.Equal(t, 4, len(serializedLogs))
	assert.Equal(t, "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log", serializedLogs[0])
	assert.Equal(t, "my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is another log", serializedLogs[1])
	assert.Equal(t, "my-computer|/home/work/b.log|2024-05-16 10:11:51 +08:00 This is a log", serializedLogs[2])
	assert.Equal(t, "my-computer|/home/work/b.log|2024-05-16 10:11:51 +08:00 This is another log", serializedLogs[3])
}

func TestFileLoggerDeserializeMultipleLogs(t *testing.T) {
	serializedLogs := []string{
		"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is a log",
		"my-computer|/home/work/a.log|2024-05-16 10:11:51 +08:00 This is another log",
		"my-computer|/home/work/b.log|2024-05-16 10:11:51 +08:00 This is a log",
		"my-computer|/home/work/b.log|2024-05-16 10:11:51 +08:00 This is another log",
	}
	logger := getLogger(FileLoggerType)
	logs, err := logger.deserializeMultiLogs(serializedLogs)
	assert.Nil(t, err)
	assert.Equal(t, logs, []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	})
}

func TestGenerateGroupKey(t *testing.T) {
	meta := &LogMeta{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
	}

	key := meta.generateGroupKey()
	assert.Equal(t, key, "my-computer|/home/work/a.log")
}

func TestParseGroupKey(t *testing.T) {
	meta := parseGroupKey("my-computer|/home/work/a.log")
	assert.Equal(t, meta.Hostname, "my-computer")
	assert.Equal(t, meta.File, "/home/work/a.log")
}

func TestGroupLogs(t *testing.T) {
	logs := []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}

	logs2 := groupLogs(logs)
	assert.Equal(t, []Log{
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/a.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
		{
			Meta: &LogMeta{
				Hostname: "my-computer",
				File:     "/home/work/b.log",
			},
			Logs: []string{
				"2024-05-16 10:11:51 +08:00 This is a log",
				"2024-05-16 10:11:51 +08:00 This is another log",
			},
		},
	}, logs2)
}
