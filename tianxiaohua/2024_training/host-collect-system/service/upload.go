package service

import (
	"context"
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"github.com/redis/go-redis/v9"
)

var ctx = context.Background()

func connectServer() *sql.DB {
	dsn := "root:123456@tcp(172.17.0.4:3306)/xiaomi_go"
	db, err := sql.Open("mysql", dsn) //open不会检验用户名和密码
	if err != nil {
		panic(err.Error())
	}

	return db
}

func redisClient() *redis.Client {
	url := "redis://@172.17.0.3:6379/0?protocol=3"
	opts, err := redis.ParseURL(url)
	if err != nil {
		panic(err)
	}

	return redis.NewClient(opts)
}

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
