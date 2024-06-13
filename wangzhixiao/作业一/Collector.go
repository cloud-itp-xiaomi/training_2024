package main

import (
	"bufio"         //用于缓冲I/O操作
	"bytes"         //用于操作字节缓冲区
	"encoding/json" //用于JSON编码和解码
	"fmt"           //用于格式化I/O
	"log"           //用于记录日志
	"net/http"      //用于HTTP客户端和服务器
	"os"            //用于操作系统功能
	"strconv"       //用于字符串和基本数据类型的转换
	"strings"       //用于字符串操作
	"time"          //用于时间操作
)

// 定义一个结构体，用于表示一个指标数据
type Metric struct {
	Metric    string  `json:"metric"`    //指标名称
	Endpoint  string  `json:"endpoint"`  //端点名称
	Timestamp int64   `json:"timestamp"` //时间戳
	Step      int     `json:"step"`      //步长
	Value     float64 `json:"value"`     //指标值
	Tags      string  `json:"tags"`      //标签    这些字段会被系列化为JSON
}

// 参数：prevIdleTime 上次的空闲时间；prevTotalTime 上次的总时间
// 返回：cpu利用率（float64),当前空闲时间（unit64），当前总时间（unit64）和错误信息（error)
func getCPUUsage(prevIdleTime, prevTotalTime uint64) (float64, uint64, uint64, error) {
	file, err := os.Open("/proc/stat") //采用os.Open打开文件
	if err != nil {                    //nil表示空值
		return 0, prevIdleTime, prevTotalTime, err
	} //返回值包括CPU利用率，传入的空闲时间，传入的总时间和错误信息
	defer file.Close() //使用defer确保文件在函数结束时关闭

	scanner := bufio.NewScanner(file)  //创建一个bufio.Scanner来逐行读取文件
	scanner.Scan()                     //读取第一行
	firstLine := scanner.Text()[5:]    // 获取读取的文本，忽略前5个字符，即去掉前面的“cpu”和两个空格
	split := strings.Fields(firstLine) //使用 strings.Fields 将文本拆分为字段，存储在切片 split 中

	//将split切片的第四个元素（空闲时间字段）转换为无符号整数 idleTime
	idleTime, err := strconv.ParseUint(split[3], 10, 64)
	if err != nil {
		return 0, prevIdleTime, prevTotalTime, err
	}

	totalTime := uint64(0)    //初始化总时间为0
	for _, s := range split { //遍历切片中的每个字段
		u, err := strconv.ParseUint(s, 10, 64) //将其转换为无符号整数并累加到 totalTime 中
		if err != nil {
			return 0, prevIdleTime, prevTotalTime, err
		}
		totalTime += u
	}

	deltaIdleTime := idleTime - prevIdleTime //计算自上次调用以来的空闲时间增量 deltaIdleTime                             //当前空闲时间
	deltaTotalTime := totalTime - prevTotalTime
	cpuUsage := (1.0 - float64(deltaIdleTime)/float64(deltaTotalTime)) * 100.0 //cpu利用率计算

	return cpuUsage, idleTime, totalTime, nil
}

// 返回内存利用率（float）和错误信息（error)
func getMemoryUsage() (float64, error) {
	file, err := os.Open("/proc/meminfo") //读取
	if err != nil {
		return 0, err
	}
	defer file.Close() //使用 defer 确保文件在函数返回时关闭，无论是否发生错误

	scanner := bufio.NewScanner(file) //使用bufio.Scanner逐行读取
	memTotal := uint64(0)             //初始化  内存总量
	memFree := uint64(0)              //初始化  空闲内存量
	buffers := uint64(0)              //初始化 缓冲区内存量
	cached := uint64(0)               //初始化  缓存内存量

	for scanner.Scan() { //使用 scanner.Scan() 逐行读取文件内容
		line := scanner.Text()         //将每行文本存储在 line 变量中
		fields := strings.Fields(line) //使用 strings.Fields 将行拆分为字段，存储在切片 fields 中
		if len(fields) < 2 {           //如果行中字段少于2个，跳过该行
			continue
		}
		if fields[0] == "MemTotal:" { //检查每行的第一个字段是否是 "MemTotal
			memTotal, err = strconv.ParseUint(fields[1], 10, 64) //如果是，将第二个字段解析为无符号整数并赋值给 memTotal
			if err != nil {
				return 0, err
			}
		}
		if fields[0] == "MemFree:" { //检查每行的第一个字段是否是 MemFree
			memFree, err = strconv.ParseUint(fields[1], 10, 64)
			if err != nil {
				return 0, err
			}
		}
		if fields[0] == "Buffers:" {
			buffers, err = strconv.ParseUint(fields[1], 10, 64)
			if err != nil {
				return 0, err
			}
		}
		if fields[0] == "Cached:" {
			cached, err = strconv.ParseUint(fields[1], 10, 64)
			if err != nil {
				return 0, err
			}
		}
	}

	//检查 memTotal 是否为 0，如果为 0，表示未能成功解析 /proc/meminfo，返回错误信息。
	if memTotal == 0 {
		return 0, fmt.Errorf("failed to parse /proc/meminfo")
	}

	memUsed := memTotal - memFree - buffers - cached
	memUsage := (float64(memUsed) / float64(memTotal)) * 100.0 //内存利用率计算

	return memUsage, nil
}

// 定义一个函数，用于将metrics切片编码为JSON数据，并通过HTTP POST请求将其发送到服务器上。
// 接收参数为包含 Metric 对象的切片 metrics，返回一个错误信息（error）
func reportMetrics(metrics []Metric) error {
	jsonData, err := json.Marshal(metrics) //使用 json.Marshal 将 metrics 切片编码为JSON格式的数据
	if err != nil {
		return err
	}
	// 发送HTTP POST 请求，将JSON数据上传到指定的服务器URL，请求的内容类型设置为 application/json，数据内容为 jsonData
	resp, err := http.Post("http://localhost:8080/api/metric/upload", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		return err
	}
	defer resp.Body.Close() //使用 defer 确保响应的 Body 在函数返回时关闭，无论是否发生错误

	// 检查服务器响应状态码，如果不是200 OK 返回错误
	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("server returned non-200 status: %s", resp.Status)
	}

	return nil //返回nil,表示报告成功
}

// 定义main函数，程序的入口点，打印开始消息
func main() {
	fmt.Println("CPU and Memory usage at 1 minute intervals:\n")

	var prevIdleTime, prevTotalTime uint64 //声明并初始化上一次的空闲时间和总时间
	//循环20次，每次获得CPU利用率和内存利用率
	for i := 0; i < 20; i++ {
		cpuUsage, idleTime, totalTime, err := getCPUUsage(prevIdleTime, prevTotalTime)
		if err != nil {
			log.Fatal(err) //使用 log.Fatal 打印错误并终止程序
		}

		memUsage, err := getMemoryUsage()
		if err != nil {
			log.Fatal(err)
		}
		// 创建metrics切片，包含CPU利用率和内存利用率两个指标，设置每个指标的详细信息
		metrics := []Metric{
			{
				Metric:    "cpu.used.percent", //指标名称
				Endpoint:  "my-computer",      //指标来源
				Timestamp: time.Now().Unix(),  //时间戳
				Step:      60,                 //收集的时间间隔 60秒
				Value:     cpuUsage,           //指标的值
				Tags:      "",                 //标签 空字符串
			},
			{
				Metric:    "mem.used.percent",
				Endpoint:  "my-computer",
				Timestamp: time.Now().Unix(),
				Step:      60,
				Value:     memUsage,
				Tags:      "",
			},
		}

		err = reportMetrics(metrics)
		if err != nil {
			log.Printf("Error reporting metrics: %v\n", err)
		}

		if i > 0 {
			fmt.Printf("%d : CPU usage: %6.3f%%, Memory usage: %6.3f%%\n", i, cpuUsage, memUsage)
		}

		prevIdleTime = idleTime
		prevTotalTime = totalTime //更新上一次的空闲时间和总时间
		time.Sleep(time.Minute)   //休眠一分钟，然后进入下一次循环
	}
}

