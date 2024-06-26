package main

import (
	"fmt"
	"hostinfo/collector"
	"hostinfo/reader"
	"hostinfo/server"
	"os"
	"sync"
	"time"
)

func main() {
	configFilePath := "config.json"
	serverAddress := ":8080"
	serverURL := "http://localhost:8080"
	offsetFilePath := "offsets.json"
	hostname, _ := os.Hostname()

	var wg sync.WaitGroup
	wg.Add(3)

	go func() {
		defer wg.Done()
		server.StartServer(serverAddress)
	}()

	go func() {
		defer wg.Done()
		collector.StartCollector(configFilePath, serverURL, offsetFilePath)
	}()

	go func() {
		defer wg.Done()

		fmt.Println("等待 10 秒以确保服务器和收集器已启动...")
		time.Sleep(10 * time.Second)

		fmt.Println("开始读取日志...")
		err := reader.QueryLogs(serverURL, hostname)

		if err != nil {
			fmt.Printf("读取日志失败: %v\n", err)
		}
	}()

	wg.Wait()
}
