package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

// TestUploadLogs 测试 uploadLogs 函数
func TestUploadLogs(t *testing.T) {
	logEntries := []LogEntry{
		{
			Hostname: "test-host",
			FilePath: "/var/miHostInfoSystem/logs/test.log",
			Logs:     []string{"test log line 1", "test log line 2", "test log line 3"},
		},
	}

	logEntriesJSON, err := json.Marshal(logEntries)
	if err != nil {
		t.Fatalf("序列化日志条目失败: %v", err)
	}

	req, err := http.NewRequest("POST", "/api/log/upload", bytes.NewReader(logEntriesJSON))
	if err != nil {
		t.Fatalf("创建请求失败: %v", err)
	}
	rr := httptest.NewRecorder()

	handler := http.HandlerFunc(uploadLogs)
	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler 返回错误状态码: 获得 %v, 预期 %v", status, http.StatusOK)
	}

	var response map[string]string
	err = json.NewDecoder(rr.Body).Decode(&response)
	if err != nil {
		t.Errorf("解析响应体失败: %v", err)
	}

	if response["message"] != "ok" {
		t.Errorf("响应消息错误: %v", response["message"])
	}
}

// TestQueryLogs 测试 queryLogs 函数
func TestQueryLogs(t *testing.T) {
	logStorage["test-host"] = []LogEntry{
		{
			Hostname: "test-host",
			FilePath: "test.log",
			Logs:     []string{"test log line 1", "test log line 2", "test log line 3"},
		},
	}

	req, err := http.NewRequest("GET", "/api/logs?hostname=test-host", nil)
	if err != nil {
		t.Fatalf("创建请求失败: %v", err)
	}
	rr := httptest.NewRecorder()

	handler := http.HandlerFunc(queryLogs)
	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler 返回错误状态码: 获得 %v, 预期 %v", status, http.StatusOK)
	}

	var logs []LogEntry
	err = json.NewDecoder(rr.Body).Decode(&logs)
	if err != nil {
		t.Errorf("解析响应体失败: %v", err)
	}

	if len(logs) != 1 {
		t.Errorf("返回日志条目数量错误: %v", len(logs))
	}

	if logs[0].Hostname != "test-host" {
		t.Errorf("日志条目主机名错误: %v", logs[0].Hostname)
	}
}
