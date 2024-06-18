package collector

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/robfig/cron"
	"host-collect-system/service"
	_ "net/http"
	"time"
)

func startCollect() {
	r := gin.Default()
	r.GET("/api/metric/upload", func(c *gin.Context) {
		collectJob()
	})
	r.Run()
}

func collectJob() {
	// 配置 Cron 任务
	c := cron.New()
	c.AddFunc("*/60 * * * * ", func() {
		upload()
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
