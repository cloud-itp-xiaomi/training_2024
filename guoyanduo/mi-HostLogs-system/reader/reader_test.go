package reader

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"hostinfo/server"
)

// TestQueryLogs 测试 queryLogs 函数
func TestQueryLogs(t *testing.T) {
	logEntries := []server.LogEntry{
		{
			Hostname: "test-host",
			FilePath: "/var/miHostInfoSystem/logs/test.log",
			Logs:     []string{"test log line 1", "test log line 2", "test log line 3"},
		},
	}

	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		json.NewEncoder(w).Encode(logEntries)
	}))
	defer server.Close()

	err := QueryLogs(server.URL, "test-host")
	if err != nil {
		t.Fatalf("查询日志失败: %v", err)
	}
}
