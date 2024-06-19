package service

import (
	"context"
	"database/sql"
	"encoding/json"
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

type LogServer interface {
	saveLog(log Log) error
	readLog(hostname string, file string) Log
	updateLog(log Log) error
	getLastUpdateTime(hostname string, file string) int64
	saveLogs(logs []Log) error
	readLogs() []Log
}

func getServer(serverType string) LogServer {
	switch serverType {
	case "mysql":
		return &MysqlServer{}
	case "file":
		return &FileServer{
			fileName: "/home/txh/work/logs.txt",
		}
	default:
		return nil
	}
}

// 序列化字符串 redis
func serialize(value interface{}) []byte {
	// 将结构体序列化为 JSON 字符串
	utilizationJSON, err := json.Marshal(value)
	if err != nil {
		panic(err)
	}
	return utilizationJSON
}

// 反序列化 redis
func deserialize(jsonString string, value interface{}) {
	err := json.Unmarshal([]byte(jsonString), value)
	if err != nil {
		panic(err)
	}
}
