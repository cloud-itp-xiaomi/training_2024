package com.lx.server.config;

import com.lx.server.utils.GetBeanUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@DependsOn(value = "getBeanUtil")
public class RedisConfig {

    RedisConnectionFactory connectionFactory = GetBeanUtil.getBean(RedisConnectionFactory.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());// 设置键的序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());// 设置值的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());// 设置哈希键的序列化器
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());// 设置哈希值的序列化器

        return template;
    }
}