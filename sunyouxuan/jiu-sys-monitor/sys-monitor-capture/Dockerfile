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
