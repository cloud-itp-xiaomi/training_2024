# 作业一
## 项目介绍
项目包括两个模块collector和server模块，其中collector模块负责定时采集主机利用率信息，并传送给API接口upload。
server模块实现upload接口来保存collector模块上传的信息，并存储到Mysql数据库中，同时需要对最新的十条数据保存到Redis中，并实时更新其中数据。
整个springboot项目运行环境在jdk17、Mysql5.7.19、Redis5.0下进行。
## 项目启动
（因DockerHub暂时无法拉取，未及时部署到docker中使用）
通过打包好的jar包，分别启动两个jar包。
### 1、运行sever模块
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

