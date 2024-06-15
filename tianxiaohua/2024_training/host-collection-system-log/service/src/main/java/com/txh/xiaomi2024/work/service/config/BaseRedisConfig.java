package com.txh.xiaomi2024.work.service.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.txh.xiaomi2024.work.service.dao.impl.RedisDaoImpl;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * GenericToStringSerializer: 可以将任何对象泛化为字符串并序列化
 * Jackson2JsonRedisSerializer: 跟JacksonJsonRedisSerializer实
 * jdkSerializationRedisSerializer: 序列化java对象际上是一样的
 * JacksonJsonRedisSerializer: 序列化object对象为json字符串
 * StringRedisSerializer: 简单的字符串序列化
 * 通过观察RedisTemplate的源码我们就可以看出来，默认使用的是JdkSerializationRedisSerializer. 这种序列化最大的问题就是存入对象后，我们很难直观看到存储的内容，很不方便我们排查问题
 * 而一般我们最经常使用的对象序列化方式是： Jackson2JsonRedisSerializer
 *
 * 设置序列化方式的主要方法就是我们在配置类中，自己来创建RedisTemplate对象，并在创建的过程中指定对应的序列化方式。
 */
@Component
public class BaseRedisConfig {
    private static final Jackson2JsonRedisSerializer<Object> JSON_SERIALIZER = createJsonRedisSerializer();


    private static Jackson2JsonRedisSerializer<Object> createJsonRedisSerializer() {
        // 创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(
                PropertyAccessor.ALL,
                JsonAutoDetect.Visibility.ANY);
        // 必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * JSON_SERIALIZER静态实例，并在 createJsonRedisSerializer() 方法中初始化了 Jackson2JsonRedisSerializer
     * 每次调用 redisSerializer() 方法时，都返回该静态实例，确保只创建一次 Jackson2JsonRedisSerializer 对象，并实现了对象的单例模式
     * @return
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return JSON_SERIALIZER;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        // 设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer())).entryTtl(Duration.ofDays(1));
        return new RedisCacheManager(redisCacheWriter,
                redisCacheConfiguration);
    }
}
