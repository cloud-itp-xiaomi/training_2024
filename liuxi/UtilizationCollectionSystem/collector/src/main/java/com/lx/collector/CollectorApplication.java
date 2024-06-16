package com.lx.collector;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CollectorApplication {


    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

}
