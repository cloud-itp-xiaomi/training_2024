package com.winter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan("com.winter")
@EnableFeignClients("com.winter.feign")
@EnableAsync //启用异步功能
public class CollectorApplication {

    //增加日志打印
    private static final Logger LOG = LoggerFactory.getLogger(CollectorApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CollectorApplication.class);
        Environment environment = app.run(args).getEnvironment();
        LOG.info("启动成功!!!");
        LOG.info("地址\thttp://127.0.0.1{}", environment.getProperty("server.port"));
    }
}
