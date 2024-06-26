# 使用官方的 Maven 镜像作为构建阶段
FROM maven:latest AS build

# 设置工作目录
WORKDIR /app

# 将 pom.xml 和应用程序的源码复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 构建项目
RUN mvn clean package -DskipTests

# 使用 OpenJDK 21 运行时镜像作为运行阶段
FROM openjdk:21

# 设置工作目录
WORKDIR /app

# 从构建阶段复制生成的 JAR 文件到运行阶段
COPY --from=build /app/target/*.jar app.jar

# 暴露应用程序的端口（根据你的应用程序的配置）
EXPOSE 8080

## 运行应用程序
CMD ["java", "-jar", "app.jar","--spring.profiles.active=prod"]

## 使用官方的 Maven 镜像作为构建阶段
#FROM maven:latest AS build
#
## 设置工作目录
#WORKDIR /app
#
## 将 pom.xml 和应用程序的源码复制到容器中
#COPY pom.xml .
#COPY src ./src
#
## 使用 Maven 构建项目
#RUN mvn clean package -DskipTests
#
## 使用 Ubuntu 作为运行阶段基础镜像
#FROM ubuntu:20.04
#
## 设置 MySQL 环境变量
#ENV MYSQL_ROOT_PASSWORD=123456
#ENV MYSQL_DATABASE=jiu-sys-monitor
#ENV DEBIAN_FRONTEND=noninteractive
#
## 安装必要的工具和 MySQL
#RUN apt-get update && apt-get install -y openjdk-21-jdk wget gnupg lsb-release && \
#    wget https://dev.mysql.com/get/mysql-apt-config_0.8.17-1_all.deb && \
#    dpkg -i mysql-apt-config_0.8.17-1_all.deb && \
#    apt-get update && \
#    apt-get install -y mysql-server && \
#    apt-get clean && rm -rf /var/lib/apt/lists/*
#
## 设置工作目录
#WORKDIR /app
#
## 从构建阶段复制生成的 JAR 文件到运行阶段
#COPY --from=build /app/target/*.jar app.jar
#
## 配置 MySQL 数据目录
#RUN mkdir -p /var/lib/mysql /var/run/mysqld && chown -R mysql:mysql /var/lib/mysql /var/run/mysqld
#
## 初始化 MySQL 数据库并停止服务
#RUN service mysql start && \
#    mysql -u root -p$MYSQL_ROOT_PASSWORD -e "CREATE DATABASE $MYSQL_DATABASE;" && \
#    service mysql stop
#
## 暴露应用程序和 MySQL 的端口
#EXPOSE 8080 3306
#
## 运行 MySQL 和 Spring Boot 应用程序
#CMD ["bash", "-c", "service mysql start && java -jar /app/app.jar --spring.profiles.active=prod"]