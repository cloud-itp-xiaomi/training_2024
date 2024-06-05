# README

## 一、项目描述

该项目是一个采集linux系统的cpu和内存利用率的项目，通过DockerCompose部署在Linux上运行，cpu-collector负责采集linux系统的cpu和内存的利用率，并将结果上传到cpu-server服务，最后cpu-server将数据存储到mysql

## 二、运行该项所需环境

linux系统、linux系统上安装Docker、Linux系统上安装DockerCompose

## 三、运行该项目的步骤

1、用idea打开项目，使用maven的插件将项目通过package打包，打包后会在对应模块下的target目录下生成app.jar文件

2、新建一个文件加cpu用于存放jar包和Dockerfile、docker-compose文件，cpu文件夹的结构如图

![](.\asset\46ED844AB6AA430343CB25215E45B3F9.png)

![](.\asset\F4CDB9E0ADF7E6D59B6916417882DD41.png)



![](.\asset\CA5A2938A1A905AB19856F1184B767B9.png)

3、将cpu-collector模块的app.jar放入cpu-collector文件夹、将cpu-server模块的app.jar放入cpu-server文件夹

4、在两个文件夹中添加Dockerfile文件，文件内容为

```
FROM java:8-alpine # 基于java:8-alpine构建镜像
COPY ./app.jar /tmp/app.jar
ENTRYPOINT java -jar /tmp/app.jar
```

5、在cpu目录下添加docker-compose.yml文件

```yaml
version: "3.2"  # dockercompose的版本

services:
  nacos:
    image: nacos/nacos-server #指定镜像
    environment:
      MODE: standalone # 单机部署模式
      TZ: "Asia/Shanghai" # 设置时区
    ports:
      - "8848:8848" # 开放端口映，左边为宿主机端口，右边为容器内端口
  mysql:
    image: mysql:5.7.25
    environment:
      MYSQL_ROOT_PASSWORD: 123 # 设置mysql root用户的密码
      TZ: "Asia/Shanghai"
    volumes:
      - "宿主机中用于存放mysql数据的目录:/var/lib/mysql" # 例如 /tmp/mysql/data
      - "宿主机中用于存放mysql配置的目录:/etc/mysql/conf.d/" # 例如 /tmp/mysql/conf
    ports: 
      - "3306:3306"
  redis: 
    image: redis:latest
    ports: 
      - "6379:6379"
    environment:
      TZ: "Asia/Shanghai"
  cpu-collector:
    build: ./cpu-collector # 基于cpu-collector文件夹的app.jar和Dockerfile构建镜像
    environment:
      TZ: "Asia/Shanghai"
    volumes:
      - "/proc/stat:/hostinfo/proc/stat" # 为了采集到宿主机的信息把宿主机上的文件夹挂载到容器内
      - "/proc/meminfo:/hostinfo/proc/meminfo"
      - "/etc/hostname:/hostinfo/proc/hostname"
  cpu-server:
    build: ./cpu-server
    ports: 
      - "8081:8081" # 开发该服务的端口，部署后可通过宿主机ip地址加端口号访问到项目中的接口
    environment:
      TZ: "Asia/Shanghai"

```

6、将cpu文件上传到虚拟机，并cd到该目录下

![](.\asset\A1B6FC9C776A1E1C378C907C0BB822CE.png)

7、执行`docker-compose up -d`命令部署服务

![](.\asset\afcc8ab7c6269911d1dd104b1fad193f.png)

8、执行`docker-compose logs -f nacos`命令查看nacos运行日志

![](.\asset\614DE948BAA2DD59F40CD8368A51B325.png)

9、当nacos启动后，可通过linux服务器ip:8848访问到nacos，发现服务并未启动成功，原因是nacos启动比cpu-collector和cpu-server慢导致服务注册不上nacos

![](.\asset\35AC15B1D67B4BF056115D43DE925B30.png)

10、重启服务`docker-compose restart cpu-server cpu-collector`

![](.\asset\E692BDF734792921AEFC6086052E2C45.png)

11、通过`docker-compose logs -f`查看日志，发现项目启动成功

![](.\asset\38bfaa57ba22d63d9c714ea18a371945.png)

12、通过Apifox请求接口

![](.\asset\8E450AF446C017A352306DBB9B52CD37.png)

![](.\asset\FC6452DC45ECCFB5176EF3302B54B3AC.png)



