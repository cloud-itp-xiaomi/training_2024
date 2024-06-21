package main 

type Metric struct {
    Metric    string  `json:"metric"`    // 度量指标的名称
    Endpoint  string  `json:"endpoint"`  // 度量指标的终端点
    Timestamp int64   `json:"timestamp"` // 时间戳
    Step      int64   `json:"step"`      // 步长
    Value     float64 `json:"value"`     // 度量指标的值
}