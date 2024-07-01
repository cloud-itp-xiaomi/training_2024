# 使用官方的OpenJDK 17作为基础镜像
FROM openjdk:17

# 将本地的app.jar添加到容器中，并重命名为app.jar
ADD xiaomi1-0.0.1-SNAPSHOT.jar server.jar

EXPOSE 8080

# 容器启动时的命令（等价于 java -jar xxxx.jar）
ENTRYPOINT ["java", "-jar", "/server.jar"]