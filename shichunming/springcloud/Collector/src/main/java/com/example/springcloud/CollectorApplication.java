package com.example.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CollectorApplication
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 00:33
 **/
@SpringBootApplication
@MapperScan("com.example.springcloud.mapper")
@EnableScheduling //开启定时任务
public class CollectorApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(CollectorApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
