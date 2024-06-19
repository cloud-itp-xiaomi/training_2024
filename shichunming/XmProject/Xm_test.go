package XmProject

import (
	"encoding/json"
	"fmt"
	"log"
	"os"
	"testing"
)

type LogConfig struct {
	Files      []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

func TestReadCfg(t *testing.T) {
	filePath := "./cfg.json"
	file, err := os.Open(filePath)
	if err != nil {
		fmt.Println(err)
	}
	file.Close()
	file1, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println(err)
	}
	var cfg LogConfig
	if err := json.Unmarshal([]byte(file1), &cfg); err != nil {
		log.Fatalf("转换json失败:%v", err)
	}

	fmt.Println("Files:")
	for _, file := range cfg.Files {
		fmt.Println(file)
	}
	fmt.Printf("Log storage:%s", cfg.LogStorage)

}

func TestReadLog(t *testing.T) {
	filePath := "./home/work/a.log"
	file, err := os.Open(filePath)
	if err != nil {
		fmt.Println(err)
	}
	defer file.Close()
	context, err := os.ReadFile(filePath)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(string(context))
}

func TestGetHostname(t *testing.T) {
	hostname, err := os.Hostname()
	if err != nil {
		fmt.Printf("Failed to get hostname: %v\n", err)
		return
	}
	fmt.Println("Hostname:", hostname)
}
