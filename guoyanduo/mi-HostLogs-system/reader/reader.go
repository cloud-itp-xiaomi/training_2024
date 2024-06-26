package reader

import (
	"encoding/json"
	"fmt"
	"hostinfo/server"
	"net/http"
	"net/url"
	"path"
)

// QueryLogs 查询日志并输出
func QueryLogs(serverURL, hostname string) error {
	u, err := url.Parse(serverURL)
	if err != nil {
		return fmt.Errorf("解析服务器URL失败: %v", err)
	}
	u.Path = path.Join(u.Path, "/api/logs")
	q := u.Query()
	q.Set("hostname", hostname)
	u.RawQuery = q.Encode()

	resp, err := http.Get(u.String())
	if err != nil {
		return fmt.Errorf("发送HTTP请求失败: %v", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("查询日志失败: %s", resp.Status)
	}

	var logs []server.LogEntry
	err = json.NewDecoder(resp.Body).Decode(&logs)
	if err != nil {
		return fmt.Errorf("解析日志响应失败: %v", err)
	}

	for _, log := range logs {
		fmt.Printf("主机名: %s\n文件: %s\n日志:\n", log.Hostname, log.FilePath)
		for _, line := range log.Logs {
			fmt.Println(line)
		}
		fmt.Println("-------")
	}

	return nil
}
