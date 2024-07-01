package collector

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/robfig/cron"
	"host-collect-system/service"
	_ "net/http"
	"os"
	"time"
)

func startCollectUtilization() {
	r := gin.Default()
	r.GET("/api/metric/upload", func(c *gin.Context) {
		collectUtilizationJob()
	})
	r.Run()
}

func collectUtilizationJob() {
	// 配置 Cron 任务
	c := cron.New()
	c.AddFunc("*/60 * * * * ", func() {
		upload()
		uploadLog()
	})
	c.Start()
	select {}
}

func upload() {
	cpu := getCPUUtilization()
	mem := getMemUtilization()
	hostname := getHostname()
	currentTime := time.Now()
	timestamp := currentTime.UnixMilli()
	fmt.Println("Current time:", currentTime)
	fmt.Println("Current time:", timestamp)
	service.UploadUtilization("cpu.used.percent", hostname, timestamp, cpu)
	service.UploadUtilization("mem.used.percent", hostname, timestamp, mem)
}

func uploadLog() {
	hostname := getHostname()
	config := getFilesAndMemType()
	logFiles := config.Files
	storageType := config.LogStorage
	for i := 0; i < len(logFiles); i++ {
		fileInfo, err := os.Stat(logFiles[i])
		if err != nil {
			panic(err.Error())
		}
		currentModiTime := fileInfo.ModTime().UnixMilli()
		fmt.Println("now:", currentModiTime)
		updated, err := isFileUpdated(hostname, logFiles[i], currentModiTime, storageType)
		if err != nil {
			panic(err.Error())
		}
		if updated {
			service.UploadLog(hostname, logFiles[i], getLogsFromLogFile(logFiles[i]), currentModiTime, storageType)
		} else {
			fmt.Println("File has not been updated.")
		}
	}
}

// 根据时间判断文件是否修改
func isFileUpdated(hostname string, filePath string, currentTime int64, storageType string) (bool, error) {

	lastUpdateTime := service.QueryLastUpdateTimeLog(hostname, filePath, storageType)
	fmt.Println("last:", lastUpdateTime)

	if currentTime > lastUpdateTime {
		return true, nil
	}

	return false, nil
}
