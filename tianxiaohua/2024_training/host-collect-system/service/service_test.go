package service

import (
	"fmt"
	"testing"
)

func TestInsertUtilization(t *testing.T) {
	utilization := Utilization{
		Metric:      "mem.used.percent",
		Endpoint:    "Slave1",
		CollectTime: 1718684154558,
		Value:       10.89,
	}
	db := connectServer()
	defer db.Close()
	insertUtilization(utilization, db)
}

func TestInsertLog(t *testing.T) {
	log := Log{
		Hostname: "Slave1",
		File:     "/home/txh/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
		},
		FileLastUpdateTime: 1718686814000,
	}
	db := connectServer()
	defer db.Close()
	insertLog(log, db)
}

func TestQueryUtilization(t *testing.T) {
	db := connectServer()
	defer db.Close()
	utilizations := queryUtilization("cpu.used.percent", "Slave1", 1718686814000, 1718704848265, db)

	for i := 0; i < len(utilizations); i++ {
		fmt.Println(utilizations[i].CollectTime, utilizations[i].Value)
	}
}

func TestQueryLog(t *testing.T) {
	db := connectServer()
	defer db.Close()
	log := queryLog("Slave1", "/home/txh/work/a.log", db)

	fmt.Println(log.Logs, log.FileLastUpdateTime)

}

func TestUpdateLog(t *testing.T) {
	log := Log{
		Hostname: "Slave1",
		File:     "/home/txh/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 This is another log",
			"2024-05-16 10:11:51 +08:00 Tssss",
			"2024-05-16 10:11:51 +08:00 Thddddd",
		},
		FileLastUpdateTime: 1718686814000,
	}
	db := connectServer()
	defer db.Close()
	updateLog(log, db)
}

func TestConnectRedis(t *testing.T) {
	redisClient := redisClient()
	defer redisClient.Close()
}

func TestRPush(t *testing.T) {
	utilization := Utilization{
		Metric:      "mem.used.percent",
		Endpoint:    "Slave1",
		CollectTime: 1718684154558,
		Value:       10.89,
	}
	// 序列化
	jsonString := serialize(utilization)

	redisClient := redisClient()
	defer redisClient.Close()
	RPush("uti_go", jsonString, redisClient)
}

func TestLRange(t *testing.T) {
	redisClient := redisClient()
	defer redisClient.Close()
	jsonStrings := LRange("uti_go", 0, 11, redisClient)

	fmt.Println(jsonStrings)
	for i := 0; i < len(jsonStrings); i++ {
		var utilization Utilization
		// 反序列化
		deserialize(jsonStrings[i], &utilization)
		fmt.Println(utilization)
	}
}

func TestLLen(t *testing.T) {
	redisClient := redisClient()
	defer redisClient.Close()
	fmt.Println(LLen("uti_go", redisClient))
}

func TestQueryAll(t *testing.T) {
	startQueryUtilization()
}

func TestFileServerSaveAndReadOneLog(t *testing.T) {
	log1 := Log{
		Hostname: "my-computer",
		File:     "/home/work/a.log",
		Logs: []string{
			"2024-05-16 10:11:51 +08:00 This is a log",
			"2024-05-16 10:11:51 +08:00 ccccc",
		},
	}
	server := FileServer{
		fileName: "logs.txt",
	}
	server.updateLog(log1)
}

func TestQueryLogs(t *testing.T) {
	startQueryLog()
}
