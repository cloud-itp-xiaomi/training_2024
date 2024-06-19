package collectorDataType

type MetricData struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int64   `json:"step"`
	Value     float64 `json:"value"`
}
