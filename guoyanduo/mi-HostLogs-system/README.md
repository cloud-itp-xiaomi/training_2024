## 作业2-主机日志采集系统

该项目实现了一个简单的日志采集上传系统，包括三个主要组件：collector、server 和 reader。

项目编码符合工程规范，且包含单元测试。

### 文件结构

```go
mi-HostLogs-system
│
├── cmd
│	└──  main.go 			//	项目的总启动代码
│
├── collector
│	├── collector.go		// collector的实现
│	├── collector_test.go	// collector的单元测试
│	└── config.json			// collector的配置文件
│
├── reader
│	├── reader.go			// reader的实现
│	└── reader_test.go		// reader的单元测试
│	└── reader.sh			// shell命令形式的reader实现（不需执行）
│
└── server
	├── server.go			// server的实现
	└── server_test.go		// server的单元测试
```

### 备注

1、由于我的作业1代码简陋，不太规范，拓展性差。故该项目没有基于作业1，而是另建了新项目。

2、目前只实现了对local_file本地文件类型日志的支持，后续会继续开发，支持从mysql中读取日志。

