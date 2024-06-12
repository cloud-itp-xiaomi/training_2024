package databaseOperate

import (
	"context"
	"encoding/json"
	"log"
	"server/dataType"

	"github.com/redis/go-redis/v9"
)

var redisDatabase *redis.Client
var ctx = context.Background()

func InitRedis(addr, password string, db int) {
	redisDatabase = redis.NewClient(&redis.Options{
		Addr:     addr,
		Password: password,
		DB:       db,
	})
}

func InsertMetricToRedis(metric dataType.MetricData) error {
	key := "metrics:" + metric.Metric
	jsonData, err := json.Marshal(metric)
	if err != nil {
		log.Println(err)
		return err
	}
	redisDatabase.LPush(ctx, key, jsonData)
	redisDatabase.LTrim(ctx, key, 0, 9)
	log.Println("metric:", metric.Metric, ",endpoint:", metric.Endpoint, ",timestamp:", metric.Timestamp, ",step:", metric.Step, ",value:", metric.Value)
	return nil
}

func QueryMetricsFromRedis(metric, endpoint string, startTs, endTs int64) ([]dataType.MetricData, int64, error) {
	key := "metrics:" + metric
	redisResults, err := redisDatabase.LRange(ctx, key, 0, -1).Result()
	if err != nil {
		return nil, 0, err
	}

	//获取redis中的每条数据，找到redis中最早的时间戳数据
	var metrics []dataType.MetricData
	var lastTimestamp int64
	lastTimestamp = 9999999999 //lastTs初始设为最大，后续redis有更小的时间戳则更新
	for _, item := range redisResults {
		var metricData dataType.MetricData
		if err := json.Unmarshal([]byte(item), &metricData); err != nil {
			log.Printf("Error unmarshalling metric data: %v", err)
			continue
		}
		if metricData.Endpoint == endpoint && metricData.Timestamp >= startTs && metricData.Timestamp <= endTs {
			metrics = append([]dataType.MetricData{metricData}, metrics...) //使数据按时间戳从前往后排序

			if metricData.Timestamp < lastTimestamp {
				lastTimestamp = metricData.Timestamp
			}
		}
	}
	return metrics, lastTimestamp, nil
}
