package databaseOperate

import (
	"context"
	"encoding/json"
	"log"
	"server/serverDataType"

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

func InsertMetricToRedis(metric serverDataType.MetricData) error {
	key := "metrics:" + metric.Metric
	jsonData, err := json.Marshal(metric)
	if err != nil {
		log.Println(err)
		return err
	}
	redisDatabase.LPush(ctx, key, jsonData)
	redisDatabase.LTrim(ctx, key, 0, 9)
	return nil
}

func QueryMetricsFromRedis(metric, endpoint string, startTs, endTs int64) ([]serverDataType.MetricData, int64, error) {
	var metrics []serverDataType.MetricData
	var lastTimestamp int64
	lastTimestamp = 9999999999 //lastTs初始设为最大，后续redis有更小的时间戳则更新

	//根据metric和endpoint是否为空决定查询逻辑
	if metric == "" {
		keys, err := redisDatabase.Keys(ctx, "metrics:*").Result()
		if err != nil {
			return nil, 0, err
		}
		var eachMetrics []serverDataType.MetricData
		for _, key := range keys {
			redisResults, err := redisDatabase.LRange(ctx, key, 0, -1).Result()
			if err != nil {
				log.Printf("Error querying metrics for key %s: %v\n", key, err)
				continue
			}
			for _, item := range redisResults {
				var metricData serverDataType.MetricData
				if err := json.Unmarshal([]byte(item), &metricData); err != nil {
					log.Printf("Error unmarshalling metric data: %v", err)
					continue
				}
				if endpoint == "" {
					if metricData.Timestamp >= startTs && metricData.Timestamp <= endTs {
						metrics = append([]serverDataType.MetricData{metricData}, metrics...) //使数据按时间戳从前往后排序
						if metricData.Timestamp < lastTimestamp {
							lastTimestamp = metricData.Timestamp
						}
					}
				} else {
					if metricData.Endpoint == endpoint && metricData.Timestamp >= startTs && metricData.Timestamp <= endTs {
						metrics = append([]serverDataType.MetricData{metricData}, metrics...) //使数据按时间戳从前往后排序
						if metricData.Timestamp < lastTimestamp {
							lastTimestamp = metricData.Timestamp
						}
					}
				}
				metrics = append(metrics, eachMetrics...)
			}
		}
	} else {
		key := "metrics:" + metric
		redisResults, err := redisDatabase.LRange(ctx, key, 0, -1).Result()
		if err != nil {
			return nil, 0, err
		}
		for _, item := range redisResults {
			var metricData serverDataType.MetricData
			if err := json.Unmarshal([]byte(item), &metricData); err != nil {
				log.Printf("Error unmarshalling metric data: %v", err)
				continue
			}
			if endpoint == "" {
				if metricData.Timestamp >= startTs && metricData.Timestamp <= endTs {
					metrics = append([]serverDataType.MetricData{metricData}, metrics...) //使数据按时间戳从前往后排序
					if metricData.Timestamp < lastTimestamp {
						lastTimestamp = metricData.Timestamp
					}
				}
			} else {
				if metricData.Endpoint == endpoint && metricData.Timestamp >= startTs && metricData.Timestamp <= endTs {
					metrics = append([]serverDataType.MetricData{metricData}, metrics...) //使数据按时间戳从前往后排序
					if metricData.Timestamp < lastTimestamp {
						lastTimestamp = metricData.Timestamp
					}
				}
			}
		}

	}

	return metrics, lastTimestamp, nil
}
