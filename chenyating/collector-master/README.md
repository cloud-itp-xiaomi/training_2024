# 数据采集系统
## 项目介绍
1. 通过Java语言开发一个主机的 CPU 利用率和内存利用率的采集系统，并支持采集主机的日志，需要使用Docker部署在Linux环境中。
2. 目标熟练使用SpringBoot、MySql、Redis、Docker、Maven、IDEA等工具，熟悉里氏替换原则。
## 配置
- Java：1.8
- IDEA：2023.1.1
- SpringBoot：2.7
- MySql：5.7
- Redis：6.0.8
- Maven：3.8.1
- 环境：CentOS 7
## 项目说明
- entity-数据库映射：三张表
- enums-枚举：返回值、指标名、存储类型
- request（请求参数实体）、response（返回内容实体）
- cache-redis方法，用于redis存取数据
- config-数据库、redis配置文件
- mapper、service、mapper/*.xml-数据库操作
- task-两个采集任务
- utils-一些其他函数
- test-测试用例
- logback.xml日志配置文件
- .yml配置文件，连接数据库和Redis注意换成虚拟机ip
- pom.xml
- .gitignore和README.md文件编写
- cfg.json文件示例
- collector.sql用于建表
## 部署与运行
1. 使用右侧Maven工具栏，选择Lifecycle->(clean->)package，双击，打jar包，在target目录下
2. 防止后期容器日志过大，引发磁盘空间不足，需要编辑/etc/docker/daemon.json，增加如下内容：
```
{
  "log-driver": "json-file",
  "log-opts": {
	"max-size": "50m",
	"max-file": "1"
    }
}
```
```
重启docker
systemctl daemon-reload
systemctl restart docker
```
3. 启动docker、启动mysql容器、redis容器
4. 在/opt 下创建目录app/pro-jar用于存放jar包，编写Dockerfile，放在同一目录下，示例如下
```
FROM openjdk:8
MAINTAINER admin
RUN mkdir -p /opt/app
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai'>/etc/timezone
WORKDIR /opt/app
ADD ./collector-1.0.jar /opt/app/
EXPOSE 5520
CMD ["java", "-XX:MetaspaceSize=256m", "-XX:MaxMetaspaceSize=256m", "-Xms3024m"
, "-Xmx3024m", "-Xmn1512m", "-Xss256k", "-XX:SurvivorRatio=8", "-XX:+UseConcMarkSweepGC"
, "-Dfile.encoding=UTF-8", "-jar", "collector-1.0.jar"]
```
5. 如果之前的旧镜像和容器存在，先删除，在docker中拉镜像和容器
```
#停止容器
docker stop pro-collector
#删除容器
docker rm -f pro-collector
#删除镜像
docker rmi -f collector:pro
#打包镜像
docker build -t collector:pro -f collector-dockerfile .
# 拉取容器
docker run -d --name pro-collector -m 3G -v /opt/logs/collector:/opt/logs/collector -v /home/work:/home/work --restart always --network host collector:pro
```
上面代码可以写成一个upgrade-collector.sh文件，和jar包放一个目录，执行./upgrade-collector.sh
6. 运行成功后，在新生成的home/work/cfg.json文件中手动编辑，存储方式有local_file和mysql两种
注：每次重新运行前，都需要把home/work文件夹删除
```
{
  "files": [
    "/home/work/a.log",
    "/home/work/b.log"
  ],
  "log_storage": "mysql"
}
```
7. 如果出现报错:Linux User limit of inotify instances reached or too many open files
   解决方案:
   参考链接:https://blog.csdn.net/Gloomyumenge/article/details/115232457
8. Postman测试(Windows环境运行) 参数自拟，截图在学习笔记中，如果部署之后测试将ip替换为虚拟机的即可
POST：http://localhost:5520/api/metric/upload
GET： http://localhost:5520/api/metric/query
POST：http://localhost:5520/api/log/upload
GET： http://localhost:5520/api/log/query
9. 注：windows环境也可运行采集，可以试运行