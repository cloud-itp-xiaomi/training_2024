package service

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

func UploadUtilization(metric string, endpoint string, collectTime int64, value float64) {
	// 参数校验
	if metric == "" || metric != "cpu.used.percent" && metric != "mem.used.percent" {
		panic("Error: Metric error")
	}

	if endpoint == "" {
		panic("Error: Endpoint cannot be empty")
	}

	if collectTime <= 0 {
		panic("Error: CollectTime must be greater than 0")
	}

	if value < 0 || value > 100 {
		panic("Error: Value must be a non-negative number")
	}

	utilization := Utilization{
		Metric:      metric,
		Endpoint:    endpoint,
		CollectTime: collectTime,
		Value:       value,
	}

	// 存储到mysql
	db := connectServer()
	defer db.Close()

	insertUtilization(utilization, db)

	// 存储到redis
	rdb := redisClient()
	defer rdb.Close()

	// 序列化
	jsonString := serialize(utilization)

	redisClient := redisClient()
	defer redisClient.Close()
	RPush("uti_go", jsonString, redisClient)

	trim("uti_go", -10, -1, redisClient)
}

func UploadLog(hostname string, file string, logs []string, lastUpdateTime int64, storageType string) {
	// 参数校验
	if hostname == "" {
		panic("Error: hostname error")
	}

	if file == "" {
		panic("Error: file error")
	}

	if lastUpdateTime < 0 {
		panic("Error: lastUpdateTime must be greater than 0")
	}

	log := Log{
		Hostname:           hostname,
		File:               file,
		Logs:               logs,
		FileLastUpdateTime: lastUpdateTime,
	}

	server := getServer(storageType)

	log1 := server.readLog(hostname, file)

	fmt.Println("host:", log1.Hostname != "")

	if log1.Hostname != "" {
		server.updateLog(log)
	} else {
		server.saveLog(log)
	}

}

func QueryLastUpdateTimeLog(hostname string, file string, storageType string) int64 {
	server := getServer(storageType)

	return server.getLastUpdateTime(hostname, file)
}
