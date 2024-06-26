package metricApi

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"sys-monitor-server/internal/db"
	"sys-monitor-server/internal/models"
)

func QueryMetrics(c *gin.Context) {
	endpoint := c.Query("endpoint")
	if endpoint == "" {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": "endpoint is required"})
		return
	}

	metric := c.Query("metricApi")
	startTs := c.Query("start_ts")
	endTs := c.Query("end_ts")

	if startTs == "" || endTs == "" {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": "start_ts and end_ts are required"})
		return
	}

	var result []models.Metric
	query := db.DB.Where("endpoint = ? AND timestamp BETWEEN ? AND ?", endpoint, startTs, endTs)
	if metric != "" {
		query = query.Where("metricApi = ?", metric)
	}

	query.Find(&result)

	data := make(map[string][]models.Metric)
	for _, r := range result {
		data[r.Metric] = append(data[r.Metric], r)
	}

	response := []gin.H{}
	for k, v := range data {
		values := []gin.H{}
		for _, value := range v {
			values = append(values, gin.H{"timestamp": value.Timestamp, "value": value.Value})
		}
		response = append(response, gin.H{"metricApi": k, "values": values})
	}

	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "ok", "data": response})
}
