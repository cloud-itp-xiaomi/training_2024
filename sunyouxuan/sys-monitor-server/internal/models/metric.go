package models

//type Metric struct {
//	ID        uint    `gorm:"primary_key"`
//	Metric    string  `json:"metricApi" binding:"required"`
//	Endpoint  string  `json:"endpoint" binding:"required"`
//	Timestamp int64   `json:"timestamp" binding:"required"`
//	Step      int64   `json:"step" binding:"required"`
//	Value     float64 `json:"value" binding:"required"`
//	Tags      string  `json:"tags"`
//}

type Metric struct {
	ID        uint    `gorm:"primary_key"`
	Metric    string  `json:"metric" binding:"required"`
	Endpoint  string  `json:"endpoint" binding:"required"`
	Timestamp int64   `json:"timestamp" binding:"required"`
	Step      int64   `json:"step" binding:"required"`
	Value     float64 `json:"value" binding:"required"`
	Tags      string  `json:"tags,omitempty"`
}

// TableName sets the insert table name for this struct type
func (Metric) TableName() string {
	return "sys_info_capture"
}
