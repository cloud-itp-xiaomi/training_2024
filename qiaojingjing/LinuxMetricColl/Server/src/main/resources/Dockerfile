#基础镜像
FROM openjdk:11.0-jre-buster

#拷贝jar包
COPY Server-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]