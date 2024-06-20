package handler

import (
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"server/serverDataType"
	"server/storageOperate"
)

var data serverDataType.LogPost

func UploadHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.Header().Set("Content-Type", "application/json")
		response := serverDataType.Response{Code: http.StatusMethodNotAllowed, Message: "Invalid request method"}
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("json encode err:", err)
			return
		}
		return
	}
	//接收数据
	if err := json.NewDecoder(r.Body).Decode(&data); err != nil {
		w.Header().Set("Content-Type", "application/json")
		response := serverDataType.Response{Code: http.StatusBadRequest, Message: "Invalid request body"}
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("json encode err:", err)
			return
		}
		return
	}
	fmt.Println("接收到数据:", data.LogInformation)
	switch data.LogStorage {
	case "local_file":
		dir := "/logs" // 映射到localLogs文件夹
		filename := "logs.txt"
		filePath := filepath.Join(dir, filename)

		file, err := os.OpenFile(filePath, os.O_APPEND|os.O_CREATE|os.O_WRONLY, 0644)
		if err != nil {
			log.Println("create logs.txt error:", err)
			return
		}
		defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				log.Println("close logs.txt error:", err)
			}
		}(file)
		for _, logInfo := range data.LogInformation {
			for _, eachLog := range logInfo.Logs {
				logEntry := fmt.Sprintf("Hostname: %s, File: %s, Log: %s\n", logInfo.Hostname, logInfo.File, eachLog)
				_, err := file.WriteString(logEntry)
				if err != nil {
					fmt.Println("Error writing log information:", err)
					continue
				}
			}
		}

	case "mysql":
		for _, eachLog := range data.LogInformation {
			if err := storageOperate.InsertLogsIntoMySQL(eachLog); err != nil {
				continue
			}
		}
	case "mongo":
		for _, eachLog := range data.LogInformation {
			fmt.Println("mongo")
			if eachLog.Logs != nil {
				if err := storageOperate.InsertLogsIntoMongo(eachLog); err != nil {
					continue
				}
			}
		}

	default:
		fmt.Println("unknown storage type:", data.LogStorage)

	}
	w.Header().Set("Content-Type", "application/json")
	response := serverDataType.Response{Code: http.StatusOK, Message: "ok"}
	err := json.NewEncoder(w).Encode(response)
	if err != nil {
		return
	}
}

func QueryHandler(w http.ResponseWriter, r *http.Request) {
	hostname := r.URL.Query().Get("hostname")
	file := r.URL.Query().Get("file")
	fmt.Println(hostname, file)

	var queryLogs []serverDataType.LogInformation
	var err error

	switch data.LogStorage {
	case "local_file":
		logsFile, err := os.Open("/logs/logs.txt")
		if err != nil {
			log.Println("open logs.json error,", err)
			return
		}
		defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				log.Println("close logs.json error,", err)
			}
		}(logsFile)

		scanner := bufio.NewScanner(logsFile)
		for scanner.Scan() {
			line := scanner.Text()
			logInfo := storageOperate.ParseLogLine(line)
			queryLogs = append(queryLogs, storageOperate.QueryLocalLogs(hostname, file, logInfo)...)
		}
		if err := scanner.Err(); err != nil {
			log.Println("reading logs file error,", err)
			response := serverDataType.QueryResponse{
				Code:    http.StatusInternalServerError,
				Message: "query local_file error!",
				Data:    nil,
			}
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			err := json.NewEncoder(w).Encode(response)
			if err != nil {
				log.Println("json encode err:", err)
				return
			}
			return
		}

	case "mysql":
		queryLogs, err = storageOperate.QueryLogsFromMySQL(hostname, file)
		if err != nil {
			response := serverDataType.QueryResponse{
				Code:    http.StatusInternalServerError,
				Message: "query mysql error!",
				Data:    nil,
			}
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			err := json.NewEncoder(w).Encode(response)
			if err != nil {
				log.Println("json encode err:", err)
				return
			}
			return
		}
	case "mongo":
		queryLogs, err = storageOperate.QueryLogsFromMongo(hostname, file)
		if err != nil {
			response := serverDataType.QueryResponse{
				Code:    http.StatusInternalServerError,
				Message: "query mysql error!",
				Data:    nil,
			}
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			err := json.NewEncoder(w).Encode(response)
			if err != nil {
				log.Println("json encode err:", err)
				return
			}
			return
		}

	default:
		fmt.Println("unknown storage type:", data.LogStorage)
	}

	//将log数据合并成以hostname和file为区分的大集合
	queryLogs = storageOperate.MergeLogs(queryLogs)

	response := serverDataType.QueryResponse{
		Code:    http.StatusOK,
		Message: "ok",
		Data:    queryLogs,
	}
	// 设置响应头和编码响应
	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(response); err != nil {
		http.Error(w, fmt.Sprintf("encoding error: %v", err), http.StatusInternalServerError)

	}
}
