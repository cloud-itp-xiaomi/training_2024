package logApi

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"sys-monitor-server/internal/db"
	"sys-monitor-server/internal/models"
)

func QueryLogs(c *gin.Context) {
	hostname := c.Query("hostname")
	file := c.Query("file")

	if hostname == "" || file == "" {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": "hostname and file are required"})
		return
	}

	var logInfo models.LogInfo
	result := db.DB.Where("hostname = ? AND file = ?", hostname, file).First(&logInfo)
	if result.Error != nil {
		c.JSON(http.StatusNotFound, gin.H{"code": 404, "message": "log not found"})
		return
	}

	var logMessages []models.LogMessage
	db.DB.Where("log_info_id = ?", logInfo.ID).Find(&logMessages)

	logs := []string{}
	for _, logMessage := range logMessages {
		logs = append(logs, logMessage.Message)
	}

	c.JSON(http.StatusOK, gin.H{
		"code":    200,
		"message": "ok",
		"data": gin.H{
			"hostname": logInfo.Hostname,
			"file":     logInfo.File,
			"logs":     logs,
		},
	})
}
