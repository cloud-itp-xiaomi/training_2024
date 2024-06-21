package serverDataType

type MetricData struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int64   `json:"step"`
	Value     float64 `json:"value"`
}

type Response struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

type QueryResponse struct {
	Code    int          `json:"code"`
	Message string       `json:"message"`
	Data    []MetricData `json:"data"`
}
