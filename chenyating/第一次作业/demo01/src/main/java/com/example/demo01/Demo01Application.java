package com.example.demo01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Demo01Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo01Application.class, args);
    }

}
