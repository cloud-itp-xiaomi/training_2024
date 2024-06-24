package metricApi

import (
	"encoding/json"
	"github.com/gin-gonic/gin"
	"net/http"
	"sys-monitor-server/internal/db"
	"sys-monitor-server/internal/models"
)

func UploadMetrics(c *gin.Context) {
	var metrics []models.Metric
	if err := c.ShouldBindJSON(&metrics); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}

	for _, metric := range metrics {
		if metric.Tags == "" {
			metric.Tags = "{}" // 默认值为空 JSON 对象
		}

		db.DB.Create(&metric)

		if metric.Metric == "cpu.used.percent" || metric.Metric == "mem.used.percent" {
			key := metric.Metric
			data, _ := json.Marshal(metric)
			db.RedisClient.LPush(db.Ctx, key, data)
			db.RedisClient.LTrim(db.Ctx, key, 0, 9)
		}
	}

	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "ok", "data": nil})
}
