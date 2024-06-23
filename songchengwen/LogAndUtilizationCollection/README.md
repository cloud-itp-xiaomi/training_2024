# 作业一
## 项目介绍
项目包括两个模块collector和server模块，其中collector模块负责定时采集主机利用率信息，并传送给API接口upload。
server模块实现upload接口来保存collector模块上传的信息，并存储到Mysql数据库中，同时需要对最新的十条数据保存到Redis中，并实时更新其中数据。
整个springboot项目运行环境在jdk17、Mysql5.7.19、Redis5.0下进行。
## 项目启动
（因DockerHub暂时无法拉取，未及时部署到docker中使用）
通过打包好的jar包，分别启动两个jar包。
### 1、运行server模块
```java
java -jar server-0.0.1-SNAPSHOT.jar
```
### 2、运行collector模块
```java
java -jar collector-0.0.1-SNAPSHOT.jar
```
### 3、在collector添加了一个手动触发采集的接口，可以检验是否正常采集数据

http://localhost:8080/api/collector/collect

### 4、查询接口

查询特定指标的主机对应的数据，例如metric=cpu.used.percent

http://localhost:8081/api/metric/query?endpoint=my-computer&start_ts=1718700358999&end_ts=1718700358999&metric=cpu.used.percent

查询所有指标的主机对应的数据

http://localhost:8081/api/metric/query?endpoint=my-computer&start_ts=1718700358999&end_ts=1718700358999

# 作业二
## 项目介绍
项目在作业一的基础上进行扩充，继续实现了日志变化的实时监测和日志信息的存储。
项目包括两个模块collector和server模块，其中collector模块负责定位到配置文件cfg.json中内容，并将分配若干线程监测到配置文件中指定日志的变化信息，如有变化新增则将新增的日志信息传送给Log的API接口upload。
server模块保存collector模块上传的日志信息，实现两种存储策略（local_file和Mysql），并且对Mysql存储方式的日志信息进行查询，实现了query接口对日志基本信息的查询。
整个springboot项目运行环境在jdk17、Mysql5.7.19、Redis5.0下进行。
## 项目启动
通过打包好的jar包，分别启动两个jar包。（暂未部署到Docker中）
### 1、在配置文件中修改日志信息的存储方式
暂时仅实现local_file和mysql两种存储方式
### 2、运行server模块
```java
java -jar server-0.0.2-SNAPSHOT.jar
```
### 3、运行collector模块
```java
java -jar collector-0.0.2-SNAPSHOT.jar
```
### 4、查询接口
当使用mysql存储方式时，可以使用查询接口。
查询对应主机和对应日志文件的所有信息。
http://localhost:8081/api/log/query?hostname=my-computer&file='path/to/log'
### 5、上报接口
可以手动上报一条日志信息。
http://localhost:8081/api/log/upload?hostname=my-computer&file="path/to/log"&logs="content"&logStorage="local_file/mysql"


