package repository

import (
	"fmt"
	"server/config"
	"server/internal/model"
)

// InsertMetric 插入指标数据
func InsertMetric(data map[string]interface{}) error {
	metric := model.Metric{
		Metric:    data["metric"].(string),
		Endpoint:  data["endpoint"].(string),
		Timestamp: int64(data["timestamp"].(float64)),
		Step:      int64(data["step"].(float64)),
		Value:     data["value"].(float64),
	}

	// 插入数据
	err := config.GetDB().Table("collection").Create(&metric).Error
	if err != nil {
		fmt.Printf("插入数据失败: %v", err)
		return err
	}

	fmt.Println("插入数据成功")
	return nil
}

// GetMetrics 查询指标数据
func GetMetrics(endpoint, metric string, startTs, endTs int64) ([]model.MetricData, error) {
	var metricDataList []model.MetricData

	query := config.GetDB().Table("collection").
		Select("metric, timestamp, value").
		Where("endpoint = ?", endpoint).
		Where("timestamp BETWEEN ? AND ?", startTs, endTs)

	if metric != "" {
		query = query.Where("metric = ?", metric)
	}

	err := query.Scan(&metricDataList).Error
	if err != nil {
		return nil, err
	}

	return metricDataList, nil
}
