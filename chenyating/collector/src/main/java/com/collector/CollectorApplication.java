package com.collector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 指定Spring Boot自动扫描组件的包路径
// 排除不需要的自动配置类,不启用Spring Boot默认的安全配置
@SpringBootApplication(scanBasePackages = {"com.collector"}, exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@MapperScan("com.collector.mapper")
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class,args);
    }
}