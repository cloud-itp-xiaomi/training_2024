package main

import (
	"context"       //提供上下文功能，用于跨API边界传递取消信号和截止时间
	"database/sql"  // 提供数据库接口
	"encoding/json" // 提供JSON编码和解码功能
	"fmt"
	"log"      // 提供日志记录功能
	"net/http" //提供HTTP客户端和服务器功能
	"time"     //提供时间相关功能

	"github.com/go-redis/redis/v8"     //提供Redis客户端
	_ "github.com/go-sql-driver/mysql" //导入MySQL驱动，但不直接使用，仅用于注册驱动
	"github.com/gorilla/mux"           //提供HTTP请求路由功能
)

// Metric represents the structure of the metric data
type Metric struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int     `json:"step"`
	Value     float64 `json:"value"`
	Tags      string  `json:"tags"`
}

// 定义查询结构体
type QueryMetric struct {
	Metric string `json:"metric"`
	Values []struct {
		Timestamp int64   `json:"timestamp"`
		Value     float64 `json:"value"`
	} `json:"values"`
}

// 声明全局变量
var (
	db  *sql.DB                //数据库连接对象，类型为 *sql.DB
	rdb *redis.Client          //Redis客户端对象，类型为 *redis.Client
	ctx = context.Background() //上下文对象，类型为 context.Context，使用 context.Background() 初始化
)

// 此函数用于初始化MySQL数据库连接
func initMySQL() {
	var err error
	//定义一个名为 dsn（数据源名称）的字符串，包含MySQL数据库的连接信息
	dsn := "user:password@tcp(127.0.0.1:3306)/metrics" //tcp(127.0.0.1:3306): 数据库的TCP地址和端口；metrics: 数据库名称
	db, err = sql.Open("mysql", dsn)                   //使用 sql.Open 函数连接MySQL数据库，传入数据库驱动名称 "mysql" 和 dsn
	if err != nil {                                    //将返回的数据库连接对象赋值给全局变量 db，将可能发生的错误赋值给 err
		log.Fatal(err)
	}

	err = db.Ping() //使用 db.Ping 函数检查数据库连接是否成功
	if err != nil {
		log.Fatal(err)
	}

	//使用 db.Exec 函数执行一条SQL语句，创建一个名为 metrics 的表（如果该表不存在）
	_, err = db.Exec(`CREATE TABLE IF NOT EXISTS metrics (
		id INT AUTO_INCREMENT PRIMARY KEY,
		metric VARCHAR(255),
		endpoint VARCHAR(255),
		timestamp BIGINT,
		step INT,
		value FLOAT,
		tags VARCHAR(255)
	)`)
	if err != nil {
		log.Fatal(err)
	}
}

// 定义了一个名为 initRedis 的函数，用于初始化 Redis 客户端连接
func initRedis() {
	rdb = redis.NewClient(&redis.Options{ //调用 redis.NewClient 函数，创建一个新的 Redis 客户端，并将其赋值给全局变量 rdb
		Addr:     "localhost:6379", //Addr 字段指定 Redis 服务器的地址
		Password: "",               // no password set
		DB:       0,                // use default DB //DB 字段指定使用的 Redis 数据库编号，这里设置为 0，表示使用默认数据库
	})
}

// 这段代码定义了一个名为 uploadMetrics 的HTTP处理函数，用于接收JSON格式的度量数据，并将其插入到MySQL数据库和Redis列表中
func uploadMetrics(w http.ResponseWriter, r *http.Request) {
	var metrics []Metric                            //声明一个名为 metrics 的空 Metric 类型切片，用于存储从请求中解码的度量数据
	err := json.NewDecoder(r.Body).Decode(&metrics) //使用 json.NewDecoder(r.Body).Decode(&metrics) 解码请求体中的JSON数据，并将解码结果存储到 metrics 中
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	for _, metric := range metrics { //遍历 metrics 切片中的每个 Metric 对象
		_, err := db.Exec(`INSERT INTO metrics (metric, endpoint, timestamp, step, value, tags)  //使用 db.Exec 执行一条SQL语句将当前 metric 对象的度量数据插入到MySQL数据库的 metrics 表中
			VALUES (?, ?, ?, ?, ?, ?)`, //SQL语句为一个参数化的插入语句，使用 ? 占位符代替实际的值，以防止SQL注入攻击
			metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value, metric.Tags)
		if err != nil {
			log.Printf("Error inserting metric into MySQL: %v\n", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		//使用 json.Marshal 将当前 metric 对象转换为JSON格式的字节切片
		jsonMetric, err := json.Marshal(metric)
		if err != nil {
			log.Printf("Error marshalling metric: %v\n", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		//使用 Redis 客户端的 LPush 方法将当前 metric 对象的JSON表示推入名为 metrics 的Redis列表。
		err = rdb.LPush(ctx, "metrics", jsonMetric).Err()
		if err != nil {
			log.Printf("Error reporting metric to Redis: %v\n", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		//使用 Redis 客户端的 LTrim 方法修剪名为 metrics 的Redis列表，保留前10个元素
		err = rdb.LTrim(ctx, "metrics", 0, 9).Err()
		if err != nil {
			log.Printf("Error trimming metrics in Redis: %v\n", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	}

	//设置响应头的Content-Type为 application/json
	w.Header().Set("Content-Type", "application/json")
	//使用 w.Write 向客户端写入JSON格式的成功响应，表示度量数据上传成功
	w.Write([]byte(`{"code": 200, "message": "Metrics uploaded successfully", "data": null}`))
}

// 这段代码定义了一个名为 queryMetrics 的HTTP处理函数，用于查询指定度量名称的最近10条度量数据，并将结果以JSON格式返回给客户端
func queryMetrics(w http.ResponseWriter, r *http.Request) {
	query := r.URL.Query().Get("metric") //从请求的URL参数中获取名为 "metric" 的值，存储到 query 变量中
	if query == "" {
		http.Error(w, "Metric query parameter is required", http.StatusBadRequest)
		return
	}

	//使用 db.Query 函数执行一条SQL查询语句，从数据库中选择最近10条指定度量名称的度量数据
	rows, err := db.Query(`SELECT metric, timestamp, value FROM metrics WHERE metric = ? ORDER BY timestamp DESC LIMIT 10`, query)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var metrics []QueryMetric   //声明一个名为 metrics 的 QueryMetric 类型切片，用于存储查询到的度量数据
	var metricValues []struct { //声明一个名为 metricValues 的结构体切片，用于暂存从数据库中读取的度量时间戳和值
		Timestamp int64   `json:"timestamp"`
		Value     float64 `json:"value"`
	}

	for rows.Next() { //遍历查询结果集中的每一行数据
		var metric string
		var timestamp int64
		var value float64
		if err := rows.Scan(&metric, &timestamp, &value); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		//将当前行的度量时间戳和值封装成一个结构体，并添加到 metricValues 切片中
		metricValues = append(metricValues, struct {
			Timestamp int64   `json:"timestamp"`
			Value     float64 `json:"value"`
		}{Timestamp: timestamp, Value: value})
	}

	//将查询到的度量数据添加到 metrics 切片中，包括度量名称和度量时间戳-值对
	metrics = append(metrics, QueryMetric{
		Metric: query,
		Values: metricValues,
	})

	if err := rows.Err(); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	//使用 json.Marshal 将 metrics 切片转换为JSON格式的字节切片
	response, err := json.Marshal(metrics)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json") //设置响应头的Content-Type为 application/json
	w.Write(response)                                  //使用 w.Write 向客户端写入JSON格式的响应数据
}

func main() {
	//调用 initMySQL() 函数和 initRedis() 函数，分别初始化MySQL数据库连接和Redis连接
	initMySQL()
	initRedis()

	r := mux.NewRouter() //创建一个新的 mux.Router 路由器实例，并将其赋值给变量 r
	//使用 r.HandleFunc 函数注册路由处理函数，第一个路由匹配 /api/metric/upload，并将其与 uploadMetrics 函数关联，处理HTTP POST请求
	r.HandleFunc("/api/metric/upload", uploadMetrics).Methods("POST")
	//第二个路由匹配 /api/metric/query，并将其与 queryMetrics 函数关联，处理HTTP GET请求
	r.HandleFunc("/api/metric/query", queryMetrics).Methods("GET")

	//创建一个 http.Server 实例，并配置其属性
	srv := &http.Server{
		Handler:      r,              //Handler 属性设置为之前创建的路由器 r，用于处理所有的HTTP请求
		Addr:         "0.0.0.0:8080", //Addr 属性设置为 "0.0.0.0:8080"，表示HTTP服务器监听所有可用的网络接口上的8080端口
		WriteTimeout: 15 * time.Second,
		ReadTimeout:  15 * time.Second, //WriteTimeout 和 ReadTimeout 属性分别设置为15秒，表示写入和读取操作的超时时间
	}

	//在控制台打印一条消息，表示HTTP服务器已经开始运行，显示服务器的地址和端口号
	fmt.Println("Server is running on http://0.0.0.0:8080")
	log.Fatal(srv.ListenAndServe()) //使用 srv.ListenAndServe() 启动HTTP服务器，并开始监听来自客户端的HTTP请求
}

