package com.winter.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/***
 * 需要将自定义的数据类型序列化，然后缓存到redis中
 * key采用String序列化方式，value采用json序列化方式
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(RedisSerializer.string());

        // 使用 Jackson 库将对象序列化为 JSON 字符串，并从 JSON 字符串反序列化为对象
        template.setValueSerializer(RedisSerializer.json());

        return template;
    }
}
