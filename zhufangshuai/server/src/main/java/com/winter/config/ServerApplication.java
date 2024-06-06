package com.winter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@ComponentScan("com.winter")
@MapperScan(value = "com.winter.mapper")
@EnableElasticsearchRepositories(basePackages = "com.winter.es")
public class ServerApplication {

    //增加日志打印功能
    private static final Logger LOG = LoggerFactory.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ServerApplication.class);
        Environment environment = application.run(args).getEnvironment();
        LOG.info("启动成功！！！");
        LOG.info("地址\thttp://127.0.0.1{}", environment.getProperty("server.port"));
    }
}
