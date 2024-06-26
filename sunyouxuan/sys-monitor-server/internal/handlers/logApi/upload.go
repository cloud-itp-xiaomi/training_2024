package logApi

import (
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"sys-monitor-server/internal/db"
	"sys-monitor-server/internal/models"
)

type LogUploadRequest struct {
	Hostname string   `json:"hostname" binding:"required"`
	File     string   `json:"file" binding:"required"`
	Logs     []string `json:"logs" binding:"required"`
}

func UploadLogs(c *gin.Context) {
	var logRequests []LogUploadRequest
	if err := c.ShouldBindJSON(&logRequests); err != nil {
		log.Printf("Failed to bind JSON: %v\n", err)
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}

	for _, logRequest := range logRequests {
		var logInfo models.LogInfo
		result := db.DB.Where("hostname = ? AND file = ?", logRequest.Hostname, logRequest.File).First(&logInfo)
		if result.Error != nil {
			logInfo = models.LogInfo{
				Hostname: logRequest.Hostname,
				File:     logRequest.File,
			}
			if err := db.DB.Create(&logInfo).Error; err != nil {
				log.Printf("Failed to insert log info into DB: %v\n", err)
				c.JSON(http.StatusInternalServerError, gin.H{"code": 500, "message": err.Error()})
				return
			}
		}

		for _, logMessage := range logRequest.Logs {
			logMessageEntry := models.LogMessage{
				LogInfoID: logInfo.ID,
				Message:   logMessage,
			}
			if err := db.DB.Create(&logMessageEntry).Error; err != nil {
				log.Printf("Failed to insert log message into DB: %v\n", err)
				c.JSON(http.StatusInternalServerError, gin.H{"code": 500, "message": err.Error()})
				return
			}
		}
	}

	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "ok", "data": nil})
}
