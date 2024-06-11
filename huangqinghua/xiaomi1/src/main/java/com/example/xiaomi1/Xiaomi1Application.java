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

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        return template;
//    }
}
