## 作业1-主机利用率采集系统

该项目实现了一个用于收集、存储和查询本地计算机CPU和内存使用率指标的系统。

后端采用Go（Golang）开发，并集成了MySQL用于数据存储，Redis用于缓存。

### 文件结构

该项目比较简陋，Collector和Server都是一个go文件，Reader是一个shell命令文件。

### 功能

- **指标收集**：定期使用 `github.com/shirou/gopsutil` 库从本地计算机收集CPU和内存使用率指标。
- **数据存储**：将收集到的指标数据存储在MySQL数据库中。
- **缓存**：利用Redis缓存指标数据，以提高查询性能。
- **HTTP API**:
  - `/api/metric/upload`：接收POST请求，包含指标数据，并将数据存储在MySQL和Redis中。
  - `/api/metric/query`：接收带有查询参数（endpoint、metric、时间戳范围）的GET请求，从MySQL中检索存储的指标数据。
