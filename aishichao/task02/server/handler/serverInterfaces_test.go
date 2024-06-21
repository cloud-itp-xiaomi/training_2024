package handler

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestUploadHandler(t *testing.T) {
	type LogInformation struct {
		Hostname string   `json:"hostname"`
		File     string   `json:"file"`
		Logs     []string `json:"logs"`
	}
	var testLogInformation LogInformation
	testLogInformation.Hostname = "mcp"
	testLogInformation.File = "access_log"
	testLogInformation.Logs = []string{"test1", "test2"}
	type LogPost struct {
		LogInformation []LogInformation `json:"logInformation"`
		LogStorage     string           `json:"logs"`
	}
	var data LogPost
	data.LogInformation = []LogInformation{testLogInformation}
	data.LogStorage = "local_file"

	// 将数据结构转换为 JSON 字符串
	jsonData, _ := json.Marshal(data)

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
			name: "test_UploadHandler",
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
