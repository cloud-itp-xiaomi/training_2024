package tdd_demo

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"strconv"
	"testing"
)

func TestGetFilesAndMemType(t *testing.T) {
	getFilesAndMemType()
}

func TestGetLogsFromLogFile(t *testing.T) {
	files := getFilesAndMemType().Files

	for _, file := range files {
		fi := getLogsFromLogFile(file)
		for _, f := range fi {
			fmt.Println(f)
		}
	}
}

func TestGetHostname(t *testing.T) {
	hostname := getHostname()
	fmt.Println("当前主机名:", hostname)
}

func TestSavaAndReadLogFile(t *testing.T) {
	logs := []Log{}
	hostname := getHostname()
	files := getFilesAndMemType().Files
	logStorage := getFilesAndMemType().LogStorage
	server := getServer1(logStorage)
	for _, file := range files {
		logs1 := getLogsFromLogFile(file)
		fmt.Println(logs1)
		log := Log{
			Hostname: hostname,
			File:     file,
			Logs:     logs1,
		}
		logs = append(logs, log)
	}
	err := server.saveLogs(logs)
	assert.Nil(t, err)
	log2 := server.readLogs()
	assert.Equal(t, logs, log2)
}

// 测试连接mysql
func TestConnectMysqlServer(t *testing.T) {
	connectServer()
}

func TestInsertLogs(t *testing.T) {
	var time uint64 = 123456
	connectServer()
	logs1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
		FileLastUpdateTime: strconv.FormatUint(time, 10),
	}
	logStorage := getFilesAndMemType().LogStorage
	server := getServer1(logStorage)
	err := server.saveLog(logs1)
	assert.Nil(t, err)

	//log2 := server.readLog()
	//assert.Equal(t, logs1, log2)
}

func TestQueryAll(t *testing.T) {
	connectServer()
	//logStorage := getFilesAndMemType().LogStorage
	//server := getServer1(logStorage)
	//err := server.saveLog(logs1)
	//assert.Nil(t, err)
	queryAll()
}
