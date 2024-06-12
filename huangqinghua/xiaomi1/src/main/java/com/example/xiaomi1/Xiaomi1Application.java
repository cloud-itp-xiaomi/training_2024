package com.example.xiaomi1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Xiaomi1Application {

    public static void main(String[] args) {
        SpringApplication.run(Xiaomi1Application.class, args);
    }
}
