package main

import (
	"github.com/gin-gonic/gin"
	"sys-monitor-server/internal/db"
	"sys-monitor-server/internal/handlers/logApi"
	"sys-monitor-server/internal/handlers/metricApi"
)

func main() {
	db.InitMySQL()
	db.InitRedis()

	r := gin.Default()

	r.POST("/api/metric/upload", metricApi.UploadMetrics)
	r.GET("/api/metric/query", metricApi.QueryMetrics)

	r.POST("/api/log/upload", logApi.UploadLogs)
	r.GET("/api/log/query", logApi.QueryLogs)
	err := r.Run(":8080")
	if err != nil {
		return
	}
}
