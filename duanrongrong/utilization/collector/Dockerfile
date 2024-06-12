# 使用官方的 OpenJDK 基础镜像
FROM openjdk:11-jre-slim

# 将 JAR 文件复制到容器中
COPY jar/collector.jar /app/collector.jar

# 设置工作目录
WORKDIR /app

# 定义容器启动时执行的命令
CMD ["java", "-jar", "/app/collector.jar"]
