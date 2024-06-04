package com.jiuth.sysmonitorserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.datasource.password:}")
    private String redisPassword;

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//
//
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setHostName("localhost"); // Change to your Redis server's hostname
//        jedisConnectionFactory.setPort(6379); // Change to your Redis server's port
//        jedisConnectionFactory.setPassword("123456"); // Set if your Redis server requires authentication
//        jedisConnectionFactory.getPoolConfig().setMaxTotal(50); // Maximum number of connections in the pool
//        jedisConnectionFactory.getPoolConfig().setMaxIdle(10); // Maximum number of idle connections in the pool
//        jedisConnectionFactory.getPoolConfig().setMinIdle(1); // Minimum number of idle connections in the pool
////        jedisConnectionFactory.getPoolConfig().setTestOnBorrow(true); // Test the connection before borrowing from the pool
////        jedisConnectionFactory.getPoolConfig().setTestOnReturn(true); // Test the connection before returning to the pool
////        jedisConnectionFactory.getPoolConfig().setTestWhileIdle(true); // Test idle connections in the pool
//        return jedisConnectionFactory;
//
//    }
//
//    @Bean
//    public RedisTemplate<String, String> redisTemplate() {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        return template;
//    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        if (!redisPassword.isEmpty()) {
            redisStandaloneConfiguration.setPassword(redisPassword);
        }

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }
}