
package com.example.xiaomi1.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("user1","user");
        Object o = redisTemplate.opsForValue().get("user1");
        System.out.println(o);
    }
}
