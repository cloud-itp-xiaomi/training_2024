# 2024_training
## 作业一
### 简介
实现了主机的 CPU 利用率和内存利用率的采集系统

### 技术栈
Spring Boot + Spring Cloud Alibaba + MybatisPlus + Redis + Mysql

### 实现原理
1. 设计 collector 和 server 两个模块。collector 负责利用率的采集并定时向 server 发送存储请求，server 中定义了两个接口，分别负责数据的持久化存储和查询。
2. 在 collector 中，通过 CPUWatcher 和 MemoryWatcher 两个类分别采集 cpu 和 内存利用率；通过 MetricCollector 进行整合两种数据并定时向 server 中的 /upload 接口发送持久化存储请求（通过 `@Scheduled` 注解实现定时任务非常方便）；
   远程调用 server 中的 /upload 接口通过使用 `openfeign` 组件实现，通过指定 `请求方式` `请求路径` `请求参数` `返回值类型` 即可基于动态代理实现远程调用，非常方便，且通过指定服务名称避免了将请求路径写死的情况。
3. 在 server 中，通过 Mybatis Plus 代码生成器即可迅速生成业务框架，数据持久化存储和查询实现起来都非常简单。
4. 缓存设计，为了实现存储最近的十条数据，可以使用 Redis 中的 `Sorted Set` 数据结构，在 Java 中为 `ZSet`，将数据的采集时间作为 score，即可实现按采集时间排序，非常方便。


## 作业二
### 简介
扩展作业一，添加采集主机⽇志的功能。

### 技术栈
Spring Boot + Spring Cloud Alibaba + MybatisPlus + Redis + Mysql + ElasticSearch

### 实现原理
1. 项目架构同作业一，只是对其进行扩展。
2. 通过 Apache commons-io 实现指定文件的监听。首先是自定义文件监听类，当文件创建或发生变化时进行文件信息的采集，使用 redis 缓存不同存储方式对应的文件采集偏移量，避免应用宕机导致采集从头开始。
3. 使用 nacos 配置中心管理 `cfg.json` 配置文件，以实现存储方式的热更新。
4. 在 server 中定义日志持久化存储和查询的接口，并根据不同的存储方式定义了两个实现类 `MysqlLogsServiceImpl` `ElasticsearchLogsServiceImpl`，通过工厂类来根据不同的存储方式给业务接口的调用者返回不同的实现类对象。

