FROM openjdk:11.0-jre-buster
WORKDIR /app
COPY Collector-1.0-jar-with-dependencies.jar /app/collector.jar
ENTRYPOINT ["java", "-jar", "/app/collector.jar"]