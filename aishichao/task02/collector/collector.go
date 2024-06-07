package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"io/ioutil"
	"log"
	"os"
	//"strconv"
	//"strings"
	//"time"
	"net/http"
)

type ConfigData struct {
	Files      []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

type LogInformation struct {
	Hostname string   `json:"hostname"`
	File     string   `json:"file"`
	Logs     []string `json:"logs"`
}

type LogPost struct {
	LogInformation []LogInformation `json:"logInformation"`
	LogStorage     string           `json:"logs"`
}

func main() {
	config, err := getConfig("config.json")
	if err != nil {
		log.Println("open config.json error,", err)
	}

	logInformation, err := getLogsInformation(config.Files)
	if err != nil {
		log.Println("read logs error:", err)
	}

	//time.Sleep(10 * time.Second)
	sendData(logInformation, config.LogStorage)

}

func getConfig(jsonFileName string) (*ConfigData, error) {
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

	tags, err := ioutil.ReadAll(file)
	if err != nil {
		log.Println("read config.json error,", err)
		return nil, err
	}

	var config ConfigData
	if err := json.Unmarshal(tags, &config); err != nil {
		log.Println("parse config.json error,", err)
		return nil, err
	}
	return &config, nil
}

//获取日志文件数据
func getLogsInformation(logPaths []string) ([]LogInformation, error) {
	//获取主机名
	hostname, err := os.Hostname()
	if err != nil {
		log.Println("Error getting hostname:", err)
		return nil, nil
	}

	//对于每个日志文件，读取每条数据
	var logInformation []LogInformation
	for _, logPath := range logPaths {
		file, err := os.Open(logPath)
		if err != nil {
			log.Println("open log file error:", logPath)
			return nil, err
		}
		defer file.Close()
		var logs []string
		scanner := bufio.NewScanner(file)
		for scanner.Scan() {
			logs = append(logs, scanner.Text())
		}
		logInformation = append(logInformation, LogInformation{
			Hostname: hostname,
			File:     logPath,
			Logs:     logs,
		})
	}

	return logInformation, nil
}

func sendData(data []LogInformation, logStorage string) {
	var logPost LogPost
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
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		log.Println("Received non-OK response:", resp.StatusCode)
	}
}
