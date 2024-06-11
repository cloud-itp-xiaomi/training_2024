package server

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

// example metric:
// "metric": "cpu.used.percent",
// "endpoint": "my-computer",
// "timestamp": 1715765640,
// "step": 60,
// "value": 60.1
type Metric struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int     `json:"step"`
	Value     float64 `json:"value"`
}

func TestValidateMetric(t *testing.T) {
	metric := &Metric{
		Metric:    "cpu.used.percent",
		Endpoint:  "my-computer",
		Timestamp: 1715765640,
		Step:      60,
		Value:     60.1,
	}
	assert.Equal(t, "cpu.used.percent", metric.Metric)
	assert.Equal(t, "my-computer", metric.Endpoint)
	assert.Equal(t, int64(1715765640), metric.Timestamp)
	assert.Equal(t, 60, metric.Step)
	assert.Equal(t, 60.1, metric.Value)
}

func TestValidateMetrics(t *testing.T) {
	var metrics []*Metric
	assert.Empty(t, metrics)
}
