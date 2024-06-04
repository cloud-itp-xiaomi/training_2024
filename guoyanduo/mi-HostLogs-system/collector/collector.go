package collector

import (
	"bufio"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"
)

// Config 定义配置文件结构
type Config struct {
	LogFiles   []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

// LogEntry 定义日志条目结构体
type LogEntry struct {
	Hostname string   `json:"hostname"`
	FilePath string   `json:"file"`
	Logs     []string `json:"logs"`
}

// LoadConfig 从配置文件加载配置
func LoadConfig(configFilePath string) (*Config, error) {
	fileData, err := os.ReadFile(configFilePath)
	if err != nil {
		return nil, fmt.Errorf("读取配置文件失败: %v", err)
	}
	var config Config
	err = json.Unmarshal(fileData, &config)
	if err != nil {
		return nil, fmt.Errorf("解析配置文件失败: %v", err)
	}
	return &config, nil
}

// LoadLogReadOffset 加载文件偏移量
func LoadLogReadOffset(offsetFilePath string) (map[string]int64, error) {
	offsets := make(map[string]int64)
	file, err := os.Open(offsetFilePath)
	if err != nil {
		if os.IsNotExist(err) {
			return offsets, nil
		}
		return nil, fmt.Errorf("加载偏移量文件失败: %v", err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		parts := strings.Split(scanner.Text(), " ")
		if len(parts) != 2 {
			continue
		}
		offset, err := strconv.ParseInt(parts[1], 10, 64)
		if err != nil {
			continue
		}
		offsets[parts[0]] = offset
	}

	if err := scanner.Err(); err != nil {
		return nil, fmt.Errorf("读取偏移量文件失败: %v", err)
	}
	return offsets, nil
}

// SaveLogReadOffset 保存文件偏移量
func SaveLogReadOffset(offsetFilePath string, offsets map[string]int64) error {
	file, err := os.Create(offsetFilePath)
	if err != nil {
		return fmt.Errorf("创建偏移量文件失败: %v", err)
	}
	defer file.Close()

	for filePath, offset := range offsets {
		fmt.Fprintf(file, "%s %d\n", filePath, offset)
	}
	return nil
}

// CollectLogs 收集配置中指定文件的日志
func CollectLogs(config *Config, offsetFilePath string) ([]LogEntry, error) {
	hostname, err := os.Hostname()
	if err != nil {
		return nil, fmt.Errorf("获取主机名失败: %v", err)
	}

	offsets, err := LoadLogReadOffset(offsetFilePath)
	if err != nil {
		return nil, fmt.Errorf("加载偏移量失败: %v", err)
	}

	var logEntries []LogEntry
	for _, filePath := range config.LogFiles {
		fileHandle, err := os.Open(filePath)
		if err != nil {
			return nil, fmt.Errorf("打开文件 %s 失败: %v", filePath, err)
		}
		defer fileHandle.Close() // 确保文件被正确关闭

		offset := offsets[filePath]
		_, err = fileHandle.Seek(offset, 0)
		if err != nil {
			return nil, fmt.Errorf("设置文件偏移量失败: %v", err)
		}

		scanner := bufio.NewScanner(fileHandle)
		var logs []string
		for scanner.Scan() {
			logs = append(logs, scanner.Text())
		}
		if err := scanner.Err(); err != nil {
			return nil, fmt.Errorf("扫描文件 %s 失败: %v", filePath, err)
		}

		newOffset, err := fileHandle.Seek(0, 1)
		if err != nil {
			return nil, fmt.Errorf("获取文件偏移量失败: %v", err)
		}
		offsets[filePath] = newOffset

		if len(logs) > 0 {
			logEntries = append(logEntries, LogEntry{
				Hostname: hostname,
				FilePath: filePath,
				Logs:     logs,
			})
		}
	}

	err = SaveLogReadOffset(offsetFilePath, offsets)
	if err != nil {
		return nil, fmt.Errorf("保存偏移量失败: %v", err)
	}

	return logEntries, nil
}

// ReportLogs 将收集的日志上报到服务器
func ReportLogs(serverURL string, logEntries []LogEntry) error {
	jsonData, err := json.Marshal(logEntries)
	if err != nil {
		return fmt.Errorf("序列化日志条目失败: %v", err)
	}

	response, err := http.Post(serverURL+"/api/log/upload", "application/json", strings.NewReader(string(jsonData)))
	if err != nil {
		return fmt.Errorf("上传日志失败: %v", err)
	}
	defer response.Body.Close()

	if response.StatusCode != http.StatusOK {
		return fmt.Errorf("报告日志失败: %s", response.Status)
	}

	return nil
}

func StartCollector(configFilePath, serverURL, offsetFilePath string) {
	config, err := LoadConfig(configFilePath)
	if err != nil {
		fmt.Printf("加载配置失败: %v\n", err)
		return
	}

	for {
		logEntries, err := CollectLogs(config, offsetFilePath)
		if err != nil {
			fmt.Printf("收集日志失败: %v\n", err)
			return
		}

		err = ReportLogs(serverURL, logEntries)
		if err != nil {
			fmt.Printf("报告日志失败: %v\n", err)
			return
		}

		time.Sleep(60 * time.Second)
	}
}
