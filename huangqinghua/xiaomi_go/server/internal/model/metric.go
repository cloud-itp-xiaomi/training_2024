package model

type Metric struct {
	Metric    string
	Endpoint  string
	Timestamp int64
	Step      int64
	Value     float64
}

type MetricData struct {
	Metric    string
	Timestamp int64
	Value     string
}

// MetricResponse 查询的回报实体
type MetricResponse struct {
	Metric string
	Data   []ValueData
}

type ValueData struct {
	Timestamp int64
	Value     string
}
