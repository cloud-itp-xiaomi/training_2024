实现作业一中全部功能，完成代码编码规范

types.go文件中定义了指标数据格式：

type Metric struct {

Metric    string  `json:"metric"`    // 度量指标的名称

Endpoint  string  `json:"endpoint"`  // 度量指标的终端点

Timestamp int64   `json:"timestamp"` // 时间戳

Step      int64   `json:"step"`      // 步长

Value     float64 `json:"value"`     // 度量指标的值

}

systemstats.go文件中实现了getCPUUsage()和getMemoryUsage()，分别用于获取CPU利

用率和内存利用率；

collector.go文件中实现了数据采集器功能，每分钟采集一次当前主机的CUP利用率和内存

利用率，并上报给Server。

运行结果每60秒输出指标数据，格式为：

[{mertric endpoint timestamp step value}]

运行输出示例如下：

[{cpu.used.percent 192.168.80.192 1717518538 60 1.9668976940965477} {mem.used.percent 192.168.80.192 1717518538 60 51.8810993343243}]

[{cpu.used.percent 192.168.80.192 1717518598 60 1.9512054998773953} {mem.used.percent 192.168.80.192 1717518598 60 51.21108139985782}]

[{cpu.used.percent 192.168.80.192 1717518658 60 1.9366928011672835} {mem.used.percent 192.168.80.192 1717518658 60 51.14857332126931}]

[{cpu.used.percent 192.168.80.192 1717518718 60 1.925208941387302} {mem.used.percent 192.168.80.192 1717518718 60 51.19714583467976}]

[{cpu.used.percent 192.168.80.192 1717518778 60 1.9115434783091745} {mem.used.percent 192.168.80.192 1717518778 60 51.09151828992439}]

server.go提供API，接受 Collector 上报数据，并将其存储至MySQL 和 Redis 中。

接受成功后运行示例如下：

2024/06/12 17:08:22 Initialization succeeded

Sending JSON data: 

[{"metric":"cpu.used.percent","endpoint":"192.168.80.192","timestamp":1718183302,"step":60,"value":2.5446083497520644},{"metric":"mem.used.percent","endpoint":"192.168.80.192","timestamp":1718183302,"step":60,"value":59.4443558045551}]

成功接收到 2 条度量指标数据

Metrics successfully reported.

client.go实现客户端功能，可以从Sever端查询数据，同时，查询成功后，client端输出查

询结果，server端返回被查询信息。

启动命令：

Server端：wang@wang-VMware-Virtual-Platform:~/gocode/test/server$ go run .

client端：wang@wang-VMware-Virtual-Platform:~/gocode/test/client$ go run client.go
