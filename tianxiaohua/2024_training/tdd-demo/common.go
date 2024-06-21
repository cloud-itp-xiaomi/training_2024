package tdd_demo

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
)

type Log struct {
	Hostname           string
	File               string
	Logs               []string
	FileLastUpdateTime string
}

type Server interface {
	saveLog(log Log) error
	readLog() Log
	saveLogs(logs []Log) error
	readLogs() []Log
}

type Config struct {
	Files      []string
	LogStorage string
}

func getLogsFromLogFile(filepath string) []string {
	file, err := os.Open(filepath)
	if err != nil {
		return []string{}
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	if err := scanner.Err(); err != nil {
		return []string{}
	}

	return lines
}

// 从配置文件获取存储类型和日志文件
func getFilesAndMemType() Config {
	// 读取JSON文件内容
	fileContent, err := ioutil.ReadFile("log.json")
	var config Config
	if err != nil {
		fmt.Println("读取文件出错:", err)
	} else {
		// 解析JSON数据
		err = json.Unmarshal(fileContent, &config)
		if err != nil {
			fmt.Println("解析JSON出错:", err)
		}
	}
	return config
}

func getHostname() string {
	hostname, err := os.Hostname()
	if err != nil {
		fmt.Println("获取主机名失败:", err)
	} else {
		return hostname
	}
	return ""
}

func getServer1(serverType string) Server {
	switch serverType {
	case "mysql":
		return &MysqlServer{}
	case "file":
		return &FileServer{
			fileName: "logs.txt",
		}
	default:
		return nil
	}
}
