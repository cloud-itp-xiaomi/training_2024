package main

import (
	"bufio"
	"bytes"
	"collector/collectorDataType"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"time"
)

var fileOffsets = make(map[string]int64)

func main() {
	for {
		config, err := getConfig("/configFiles/config.json")
		if err != nil {
			log.Println("open config.json error,", err)
		}
		loadOffsets(config.LogStorage)
		logInformation, err := getLogsInformation(config.Files)
		if err != nil {
			log.Println("read logs error:", err)
		}
		saveOffsets(config.LogStorage)
		sendData(logInformation, config.LogStorage)
		time.Sleep(10 * time.Second)
	}

}

func getConfig(jsonFileName string) (*collectorDataType.ConfigData, error) {
	file, err := os.Open(jsonFileName)
	if err != nil {
		log.Println("open config.json error,", err)
		return nil, err
	}
	defer func(file *os.File) {
		err := file.Close()
		if err != nil {

		}
	}(file)

	tags, err := io.ReadAll(file)
	if err != nil {
		log.Println("read config.json error,", err)
		return nil, err
	}

	var config collectorDataType.ConfigData
	if err := json.Unmarshal(tags, &config); err != nil {
		log.Println("parse config.json error,", err)
		return nil, err
	}
	return &config, nil
}

// 获取日志文件数据
func getLogsInformation(logPaths []string) ([]collectorDataType.LogInformation, error) {
	hostname, err := os.Hostname()
	if err != nil {
		log.Println("Error getting hostname:", err)
		return nil, nil
	}

	//对于每个日志文件，读取每条数据
	var logInformation []collectorDataType.LogInformation
	for _, logPath := range logPaths {
		file, err := os.Open(logPath)
		if err != nil {
			log.Println("open log file error:", logPath)
			return nil, err
		}

		offset, exists := fileOffsets[logPath]
		if !exists {
			offset = 0
		}

		_, err = file.Seek(offset, 0)
		if err != nil {
			log.Println("seek log file error:", logPath)
			return nil, err
		}

		var logs []string
		scanner := bufio.NewScanner(file)
		for scanner.Scan() {
			logs = append(logs, scanner.Text())
		}
		//记录文件偏移量
		newOffset, err := file.Seek(0, 1)
		if err != nil {
			log.Println("get new offset error:", logPath)
			return nil, err
		}
		fileOffsets[logPath] = newOffset
		fmt.Println("offset:", fileOffsets[logPath])

		logInformation = append(logInformation, collectorDataType.LogInformation{
			Hostname: hostname,
			File:     logPath,
			Logs:     logs,
		})
		err = file.Close()
		if err != nil {
			log.Println("close log file error:", logPath)
			return nil, err
		}
	}
	return logInformation, nil
}

func sendData(data []collectorDataType.LogInformation, logStorage string) {
	var logPost collectorDataType.LogPost
	logPost.LogInformation = data
	logPost.LogStorage = logStorage
	jsonData, err := json.Marshal(logPost)
	if err != nil {
		log.Println("Error marshalling data:", err)
		return
	}

	resp, err := http.Post("http://server:8080/api/log/upload", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		//log.Println("Error sending data:", err)
		return
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			log.Println("close send data body error:")
		}
	}(resp.Body)

	if resp.StatusCode != http.StatusOK {
		log.Println("Received non-OK response:", resp.StatusCode)
	}
}

func loadOffsets(storage string) {
	dir := "/offsetsFiles" // 映射到localLogs文件夹
	filename := storage
	suffix := ".json"
	offsetsFilePath := filepath.Join(dir, filename) + suffix
	if _, err := os.Stat(offsetsFilePath); os.IsNotExist(err) {
		log.Println("offsets file not exist:", offsetsFilePath)
		return
	}

	data, err := os.ReadFile(offsetsFilePath)
	if err != nil {
		log.Println("Error reading offsets file:", err)
		return
	}
	fmt.Println("load:", string(data))

	err = json.Unmarshal(data, &fileOffsets)
	if err != nil {
		log.Println("Error parsing offsets file:", err)
	}
}

func saveOffsets(storage string) error {
	dir := "/offsetsFiles" // 映射到localLogs文件夹
	filename := storage
	suffix := ".json"
	offsetsFilePath := filepath.Join(dir, filename) + suffix
	file, err := os.Create(offsetsFilePath)
	if err != nil {
		return fmt.Errorf("failed to create offset file: %w", err)
	}
	defer file.Close()

	encoder := json.NewEncoder(file)
	encoder.SetIndent("", "  ") // 格式化输出
	if err := encoder.Encode(fileOffsets); err != nil {
		return fmt.Errorf("failed to encode offsets to JSON: %w", err)
	}

	return nil
}
