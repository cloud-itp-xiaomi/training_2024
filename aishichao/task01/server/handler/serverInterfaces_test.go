package handler

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestUploadHandler(t *testing.T) {
	type MetricData struct {
		Metric    string  `json:"metric"`
		Endpoint  string  `json:"endpoint"`
		Timestamp int64   `json:"timestamp"`
		Step      int64   `json:"step"`
		Value     float64 `json:"value"`
	}
	var testMetricData MetricData
	testMetricData.Metric = "mem.used.percent"
	testMetricData.Endpoint = "mcp"
	testMetricData.Timestamp = 1718180000
	testMetricData.Step = 60
	testMetricData.Value = 0.1
	jsonData, _ := json.Marshal(testMetricData)
	type args struct {
		w http.ResponseWriter
		r *http.Request
	}
	tests := []struct {
		name string
		args args
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				w: httptest.NewRecorder(),
				r: httptest.NewRequest(http.MethodPost, "/", bytes.NewReader(jsonData)),
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			UploadHandler(tt.args.w, tt.args.r)
		})
	}
}

func TestQueryHandler(t *testing.T) {
	type args struct {
		w http.ResponseWriter
		r *http.Request
	}
	tests := []struct {
		name string
		args args
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			QueryHandler(tt.args.w, tt.args.r)
		})
	}
}
