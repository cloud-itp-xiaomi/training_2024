## 田小花2024_training

### host-collection-system项目

- collector：实现采集系统cpu和内存利用率，并上报给service
- interface：dubbo接口，提供数据上报接口，用于service模块和collector模块通信
- service：实现数据存储到mysql和redis，并进行查询



### optimization-code项目

- 对示例代码getUserDirectories的优化

tdd-demo

- 对作业2 的测试驱动开发
- txh_test.go用于测试

### host-collection-system-log

- 在作业1的基础上扩展，写的作业2
- collector：实现采集系统cpu、内存利用率和日志，并上报给service
- interface：dubbo接口，提供数据上报接口，用于service模块和collector模块通信
- service：实现数据存储到mysql和redis，日志也可以存储到elasticsearch并进行查询

```
host-collection-system-log
├── collector
│   ├── pom.xml
│   └── src
├── interface
│   ├── pom.xml
│   └── src
├── pom.xml
└── service
    ├── pom.xml
    └── src
```

**以下内容为docker安装环境，配置文件yaml的ip改为自己电脑docker安装的环境的ip**

- docker安装mysql

  docker拉取mysql镜像

  ```Shell
  sudo docker pull mysql:latest
  ```

  运行mysql容器

  ```
  docker run -itd --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql
  ```

  配置文件yaml配置mysql，ip为mysql容器ip:`172.17.0.4`

  ```
  spring:
    datasource:
      username: root
      password: 123456
      url: jdbc:mysql://172.17.0.4:3306/xiaomi?serverTimezone=GMT%2B8&useSSL=FALSE&allowPublicKeyRetrieval=true
      driver-class-name: com.mysql.cj.jdbc.Driver
  ```

- docker安装redis

  拉取镜像

  ```
   docker pull redis:latest
  ```

  运行容器

  ```
  docker run -itd --name redis-test -p 6379:6379 redis
  ```

  配置文件yaml配置redis，ip为redis容器ip:`172.17.0.3`

  ```
  redis:
      host: 172.17.0.3 #本机docker安装的Redis的ip172.17.0.3
      port: 6379
  ```

- docker安装elasticsearch

  拉取镜像

  ```
  docker pull elasticsearch:7.6.0
  ```

  运行容器

  ```
  // 创建持久化目录
  mkdir -p /home/txh/workspace/elasticsearch
  cd /home/txh/workspace/elasticsearch
  mkdir data
  mkdir logs
  mkdir plugins
  // 运行容器
  docker run --name es -p 9200:9200 -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "cluster.name=elasticsearch" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx1024m" \
  -v /home/txh/workspace/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
  -v /home/txh/workspace/elasticsearch/data:/usr/share/elasticsearch/data \
  -v /home/txh/workspace/elasticsearch/logs:/usr/share/elasticsearch/logs \
  -d elasticsearch:7.6.0
  ```

  配置文件

  ```
  # es 配置
  elasticsearch:
    ip: 172.17.0.5 # 本地docker
    port: 9200
  ```

**以下内容为项目打包**

- 项目打包：clean+package

- 打包完成在target临时目录下生成jar文件：work2-collector.jar和work2-service.jar

  ```dockerfile
  host-collection-system-log
  ├── collector
  │   ├── pom.xml
  │   ├── src
  │   │   └── main
  │   └── target
  │       ├── classes
  │       ├── generated-sources
  │       ├── maven-archiver
  │       ├── maven-status
  │       ├── work2-collector.jar
  │       └── work2-collector.jar.original
  ├── interface
  │   ├── pom.xml
  │   ├── src
  │   │   └── main
  │   └── target
  │       ├── classes
  │       ├── generated-sources
  │       ├── maven-archiver
  │       ├── maven-status
  │       └── work1-service.jar
  ├── pom.xml
  ├── dockerfile
  └── service
      ├── pom.xml
      ├── src
      │   └── main
      └── target
          ├── classes
          ├── generated-sources
          ├── maven-archiver
          ├── maven-status
          ├── work2-service.jar
          └── work2-service.jar.original
  
  ```

**以下内容为docker部署**

- `host-collection-system-log`目录下的dockerfile文档构建镜像（这里必须在dockerfile所在的目录下构建，最好是把dockerfile放在一个空目录）

  ```
  docker build -t metric_log .
  ```

- docker部署service（ps:挂载目录根据自己的目录修改）

  这里log文件不直接挂载的原因是，`/home/txh/work/a.log:/home/txh/work/a.log`这样挂载当本地文件修改时，容器内的文件不会发生改变，通过挂载目录可以解决这个问题

  `/home/txh/work/log`下放`a.log`和 `b.log`

  ```
  docker run --name work2-service -p 8083:8083 -v /home/txh/work/work2-service.jar:/home/work/work2-service.jar -v /home/txh/work/log:/home/txh/work metric_log java -jar work2-service.jar
  ```

- docker部署collector

  ```
  docker run --name work2-collector -p 8082:8082 -v /home/txh/work/work2-collector.jar:/home/work/work2-collector.jar -v /home/txh/work/log:/home/txh/work metric_log java -jar work2-collector.jar
  ```

- 访问

  访问collector的上报接口，192.168.146.130为宿主机的ip

  ```
  访问http://192.168.146.130:8082/api/log/upload开始上报日志
  访问http://192.168.146.130:8082/api/metric/upload开始上报利用率
  ```

  访问service的利用率查询接口

  ```
  访问http://192.168.146.130:8080/api/metric/query?metric=mem.used.percent&endpoint=Slave1&start_ts=1718686814000&end_ts=1718704848265&current_page=2&page_size=4，查询利用率，参数值自己修改。current_page=2&page_size=4是分页查询的条件，current_page为当前页码，page_size=4为每页的数量
  ```

  访问service的日志查询接口

  ```
  如果在collector的log.json文件设置的日志存储方式是mysql，http://192.168.146.130:8080/api/log/query?hostname=0c9bd49f65bd&file=/home/txh/work/a.log&current_page=2&page_size=4
  如果在collector的log.json文件设置的日志存储方式是elasticsearch，http://192.168.146.130:8080/api/log/query_es?hostname=0c9bd49f65bd&file=/home/txh/work/a.log&current_page=2&page_size=4
  ```

### host-collect-system

- 该项目模块是用go实现的作业

- 目录结构如下

  ```go
  .
  ├── collector 
  │   ├── collect.go  // 实现定时上报的接口
  │   ├── collect_test.go // 测试
  │   ├── go.mod // mudule
  │   ├── go.sum
  │   ├── log_collect.go // 日志采集
  │   ├── log.json // 配置文件
  │   └── utilization_collect.go // 利用率采集
  ├── collector.iml
  ├── go.mod
  ├── go.sum
  ├── host-collect-system.iml
  ├── service
  │   ├── bean.go // 定义的数据库存储字段的映射bean
  │   ├── common.go // server接口与redis序列化的定义
  │   ├── file_server.go // log存到文件
  │   ├── go.mod
  │   ├── go.sum
  │   ├── mysql_dao.go // mysql数据库操作
  │   ├── mysql_server.go // 将日志存到mysql
  │   ├── query.go // 检索
  │   ├── redis_dao.go // redis操作
  │   ├── service.go // 总的接口实现
  │   ├── service_test.go // 测试
  │   └── upload.go // 上报，collector调用这里的上报函数
  ├── service.iml
  └── todo // 自己要做的任务列表
  ```

