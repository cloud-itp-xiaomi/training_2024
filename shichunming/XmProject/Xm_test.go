package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strings"
	"testing"
)

type LogConfig struct {
	Files      []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

func ReadCfg(filePath string) LogConfig {
	file, err := ReadLogFile(filePath)
	if err != nil {
		log.Fatalln(err)
		return LogConfig{}
	}
	var cfg LogConfig
	if err := json.Unmarshal([]byte(file), &cfg); err != nil {
		log.Fatalf("转换json失败:%v \n", err)
		return LogConfig{}
	}
	return cfg
}

func ReadLogFile(filePath string) (string, error) {
	file, err := os.Open(filePath)
	if err != nil {
		log.Fatalln(err)
		return "", err
	}
	defer file.Close()
	context, err := os.ReadFile(filePath)
	if err != nil {
		log.Fatalln(err)
		return "", err
	}
	return string(context), nil
}

func GetHostName() string {
	hostname, err := os.Hostname()
	if err != nil {
		log.Fatalf("Failed to get hostname: %v\n", err)
		return ""
	}
	return hostname
}

func TestReadCfg(t *testing.T) {
	filePath := "./cfg.json"
	file, err := os.Open(filePath)
	if err != nil {
		fmt.Println(err)
	}
	file.Close()
	file1, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println(err)
	}
	var cfg LogConfig
	if err := json.Unmarshal([]byte(file1), &cfg); err != nil {
		fmt.Printf("转换json失败:%v", err)
	}

	fmt.Println("Files:")
	for _, file := range cfg.Files {
		fmt.Println(file)
	}
	fmt.Printf("Log storage:%s", cfg.LogStorage)

}

func TestReadLog(t *testing.T) {
	filePath := "./home/work/a.log"
	file, err := os.Open(filePath)
	if err != nil {
		fmt.Println(err)
	}
	defer file.Close()
	context, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(string(context))
}

func TestGetHostname(t *testing.T) {
	hostname, err := os.Hostname()
	if err != nil {
		fmt.Printf("Failed to get hostname: %v\n", err)
		return
	}
	fmt.Println("Hostname:", hostname)
}

type UploadRequest struct {
	hostname string
	file     string
	logs     []string
}

func LogUploadRequest(filePath string) UploadRequest {
	if filePath == "" {
		return UploadRequest{}
	}
	file, err := ReadLogFile(filePath)
	if err != nil {
		log.Fatalln(err)
		return UploadRequest{}
	}
	var request UploadRequest
	request.logs = strings.Split(string(file), "\r\n")
	hostname, err := os.Hostname()
	if err != nil {
		fmt.Printf("Failed to get hostname: %v\n", err)
	}
	request.hostname = hostname
	request.file = filePath
	return request
}
func TestReadLogToRequest(t *testing.T) {
	filePath := "./home/work/a.log"
	file, err := os.Open(filePath)
	if err != nil {
		fmt.Println(err)
	}
	defer file.Close()
	context, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println(err)
	}
	//fmt.Println(string(context))
	var request UploadRequest
	request.logs = strings.Split(string(context), "\r\n")
	fmt.Println(request.logs[0])
	fmt.Println(request.logs[1])
	hostname, err := os.Hostname()
	if err != nil {
		fmt.Printf("Failed to get hostname: %v\n", err)
	}
	request.hostname = hostname
	request.file = filePath
	fmt.Printf("%s %s %v", request.hostname, request.file, request.logs[0])
}

func TestUploadPost(t *testing.T) {
	data := map[string]interface{}{
		"log": "这是一个日志消息",
	}
	jsonData, err := json.Marshal(data)
	if err != nil {
		fmt.Printf("JSON编码错误: %v\n", err)
		return
	}

	url := "http://192.168.217.129:9090/api/log/upload"
	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		fmt.Printf("创建请求错误: %v\n", err)
		return
	}
	req.Header.Set("Content-Type", "application/json")
	// 发送请求
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("请求错误: %v\n", err)
		return
	}
	defer resp.Body.Close()
	// 读取并打印响应
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("读取响应错误: %v\n", err)
		return
	}
	fmt.Println("响应状态码:", resp.StatusCode)
	fmt.Println("响应内容:", string(body))
}

func PostRequest(url string, request UploadRequest) *http.Response {
	jsonData, _ := json.Marshal(request)
	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	req.Header.Set("Content-Type", "application/json")
	// 发送请求
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("请求错误: %v\n", err)
		return resp
	}
	defer resp.Body.Close()
	// 读取并打印响应
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("读取响应错误: %v\n", err)
		return resp
	}
	fmt.Println("响应状态码:", resp.StatusCode)
	fmt.Println("响应内容:", string(body))
	return resp
}

func TestHelloHandler_ServeHTTP(t *testing.T) {
	data := map[string]interface{}{
		"log": "这是一个日志消息",
	}
	jsonData, _ := json.Marshal(data)
	url := "http://localhost:8081/"
	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	req.Header.Set("Content-Type", "application/json")
	// 发送请求
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("请求错误: %v\n", err)
		return
	}
	defer resp.Body.Close()
	// 读取并打印响应
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("读取响应错误: %v\n", err)
		return
	}
	fmt.Println("响应状态码:", resp.StatusCode)
	fmt.Println("响应内容:", string(body))
}
