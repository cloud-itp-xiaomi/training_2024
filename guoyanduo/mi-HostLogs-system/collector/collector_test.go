package collector

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"os"
	"testing"
)

const testConfig = `{
	"files": ["/var/miHostInfoSystem/logs/test.log"],
	"log_storage": "local_file"
}`

const testLogContent = `test log line 1
test log line 2
test log line 3`

// TestLoadConfig 测试 LoadConfig 函数
func TestLoadConfig(t *testing.T) {
	configFilePath := "test_config.json"
	err := os.WriteFile(configFilePath, []byte(testConfig), 0644)
	if err != nil {
		t.Fatalf("无法写入测试配置文件: %v", err)
	}
	defer os.Remove(configFilePath)

	config, err := LoadConfig(configFilePath)
	if err != nil {
		t.Fatalf("加载配置失败: %v", err)
	}

	if len(config.LogFiles) != 1 || config.LogFiles[0] != "test.log" {
		t.Errorf("配置文件解析错误: %v", config)
	}

	if config.LogStorage != "/tmp/logs" {
		t.Errorf("配置文件解析错误: %v", config)
	}
}

// TestCollectLogs 测试 CollectLogs 函数
func TestCollectLogs(t *testing.T) {
	logFilePath := "test.log"
	err := os.WriteFile(logFilePath, []byte(testLogContent), 0644)
	if err != nil {
		t.Fatalf("无法写入测试日志文件: %v", err)
	}
	defer os.Remove(logFilePath)

	offsetFilePath := "offsets.json"
	defer os.Remove(offsetFilePath)

	config := &Config{
		LogFiles:   []string{logFilePath},
		LogStorage: "/tmp/logs",
	}

	logEntries, err := CollectLogs(config, offsetFilePath)
	if err != nil {
		t.Fatalf("收集日志失败: %v", err)
	}

	if len(logEntries) != 1 {
		t.Errorf("日志条目数量错误: %v", len(logEntries))
	}

	if logEntries[0].FilePath != logFilePath {
		t.Errorf("日志条目文件路径错误: %v", logEntries[0].FilePath)
	}

	if len(logEntries[0].Logs) != 3 {
		t.Errorf("日志条目内容数量错误: %v", len(logEntries[0].Logs))
	}

	// 第二次调用 CollectLogs，不应该收集到任何日志，因为没有新的日志写入
	logEntries, err = CollectLogs(config, offsetFilePath)
	if err != nil {
		t.Fatalf("收集日志失败: %v", err)
	}

	if len(logEntries) != 0 {
		t.Errorf("日志条目数量错误: %v", len(logEntries))
	}
}

// TestReportLogs 测试 ReportLogs 函数
func TestReportLogs(t *testing.T) {
	logEntries := []LogEntry{
		{
			Hostname: "test-host",
			FilePath: "test.log",
			Logs:     []string{"test log line 1", "test log line 2", "test log line 3"},
		},
	}

	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		var receivedLogs []LogEntry
		err := json.NewDecoder(r.Body).Decode(&receivedLogs)
		if err != nil {
			t.Errorf("解析请求体失败: %v", err)
		}

		if len(receivedLogs) != len(logEntries) {
			t.Errorf("接收到的日志条目数量错误: %v", len(receivedLogs))
		}

		w.WriteHeader(http.StatusOK)
	}))
	defer server.Close()

	err := ReportLogs(server.URL, logEntries)
	if err != nil {
		t.Errorf("报告日志失败: %v", err)
	}
}
