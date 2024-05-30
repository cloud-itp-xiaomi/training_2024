package com.example.xiaomi1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/test-redis")
    public String testRedis() {
        // 设置一个键值对
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");

        // 获取值
        String value = (String) redisTemplate.opsForValue().get("testKey");

        return "Value for 'testKey': " + value;
    }
}
