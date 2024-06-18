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

func TestQueryUtilization(t *testing.T) {
	db := connectServer()
	defer db.Close()
	utilizations := queryUtilization("cpu.used.percent", "Slave1", 1718686814000, 1718704848265, db)

	for i := 0; i < len(utilizations); i++ {
		fmt.Println(utilizations[i].CollectTime, utilizations[i].Value)
	}
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
	startQuery()
}
