package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"time"
)

type LogConfig struct {
	Files      []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

type LogData struct {
	Hostname string
	File     string
	Logs     []string
}

type LogPost struct {
	LogStorage string    `json:"log_storage"`
	LogData    []LogData `json:"logdata"`
}

func main() {
	//new map for filename, modify time
	fileModTimes := make(map[string]time.Time)

	for {
		//get filenames from configfile
		config, err := getConfigMessage("config.json")
		if err != nil {
			return
		}
		//jugle file change or not
		filenames, err := hasFileChange(config.Files, fileModTimes)
		if err != nil {
			return
		}
		if len(filenames) == 0 {
			time.Sleep(20 * time.Second)
			continue
		}
		//get logfile text
		logData, err := getLogsData(filenames)
		if err != nil {
			return
		}
		//send data
		sendData(logData, config.LogStorage)
		// for i, data := range logData {
		// 	fmt.Printf("LogData %d: %v\n", i, data)
		// }
		time.Sleep(20 * time.Second)
	}
	// for i, data := range logData {
	//     fmt.Printf("LogData %d: %v\n", i, data)
	// }
}

func hasFileChange(filenames []string, fileModTimes map[string]time.Time) ([]string, error) {

	var retfiles []string
	for _, filename := range filenames {
		//get file lastmodify time
		fileInfo, err := os.Stat(filename)
		if err != nil {
			return retfiles, err
		}
		modTime := fileInfo.ModTime()

		// first find map has filename or not
		if _, ok := fileModTimes[filename]; !ok {
			fileModTimes[filename] = modTime
			retfiles = append(retfiles, filename)
		}

		// compare the time
		if modTime.After(fileModTimes[filename]) {
			fileModTimes[filename] = modTime
			retfiles = append(retfiles, filename)
		}
	}
	return retfiles, nil
}

func getConfigMessage(configFilePath string) (*LogConfig, error) {

	jsonData, err := ioutil.ReadFile(configFilePath)

	if err != nil {
		log.Println("Error reading JSON file:", configFilePath, err)
		return nil, err
	}
	var config LogConfig
	err = json.Unmarshal(jsonData, &config)
	if err != nil {
		log.Println(configFilePath, "Error unmarshalling JSON:", err)
		return nil, err
	}
	return &config, nil

}

func getLogsData(logPaths []string) ([]LogData, error) {
	hostname, err := os.Hostname()
	if err != nil {
		log.Println("get hostname err", err)
		return nil, err
	}

	var LogDatas []LogData
	for _, logPath := range logPaths {
		file, err := os.Open(logPath)
		if err != nil {
			log.Println("openr file err", err)
			return nil, err
		}
		defer file.Close()

		scanner := bufio.NewScanner(file)
		var logs []string
		for scanner.Scan() {
			logs = append(logs, scanner.Text())
		}
		data := LogData{
			Hostname: hostname,
			File:     logPath,
			Logs:     logs,
		}
		LogDatas = append(LogDatas, data)
	}
	return LogDatas, nil
}

func sendData(data []LogData, log_storage string) {
	var logPost LogPost
	logPost.LogData = data
	logPost.LogStorage = log_storage
	jsonData, err := json.Marshal(logPost)
	if err != nil {
		log.Println("Error marshalling data:", err)
		return
	}

	resp, err := http.Post("http://localhost:8081/api/metric/upload", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		log.Println("Error connnect url:", err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		log.Println("err statu:", resp.StatusCode)
	}
}
