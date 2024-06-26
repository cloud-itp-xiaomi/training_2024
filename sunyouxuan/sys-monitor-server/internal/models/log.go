package models

type LogInfo struct {
	ID       uint   `gorm:"primary_key"`
	Hostname string `json:"hostname" binding:"required"`
	File     string `json:"file" binding:"required"`
}

type LogMessage struct {
	ID        uint   `gorm:"primary_key"`
	LogInfoID uint   `json:"log_info_id"`
	Message   string `json:"message" binding:"required"`
}
