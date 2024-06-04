# 1. 项目描述

本项目在CentOS 7.6系统下,基于Java开发，部署在服务器上运行，用于采集计算机cpu和内存的利用率以及采集中产生的日志文件，并可在Shell终端上查询数据。

# 2. 项目架构

# 3. 项目部署、运行

## 3.1 依赖环境

 Jdk：1.8

Maven：3.9.3

Git：2.42.3（个人视情况选择）

Docker：容器管理

--MySQL：数据库

--Redis：缓存

--Jenkins：项目自动部署工具

--xxl-job：定时任务管理

--Nacos：动态配置（未开发）

--Elasticsearch：数据存储与检索（未开发）

## 3.2 项目部署及运行（Jenkins+shell脚本）

上述docker相关镜像容器默认已经安装好了并配好对应端口（服务器防火墙记得开放）

- Docker安装Jenkins

```Shell
docker search jenkins
docker pull jenkins/jenkins:2.414.2
docker run -d -u root -p 8080:8080 -p 50000:50000 -v /var/jenkins_home:/var/jenkins_home -v /etc/localtime:/etc/localtime --name jenkins jenkins/jenkins:2.414.2
docker logs afdc75a54567     //查看Jenkins启动日志获取管理员密码
```

- Jenkins全局Maven

注意：maven一定要放到Jenkins的数据挂载目录内，这样容器才能读到。

```
cp -r  你的服务器Maven路径  /var/jenkins_home/
```

![sunmaolin/images/1.jpg](C:\Users\32536\Desktop\main\training_2024\sunmaolin\images\1.jpg)

