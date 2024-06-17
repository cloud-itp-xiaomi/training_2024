package com.example.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName ReaderApplication
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 19:07
 **/
@SpringBootApplication
public class ReaderApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ReaderApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
