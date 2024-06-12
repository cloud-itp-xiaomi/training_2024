package com.example.mi1.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private String host;
    private int port;
    private int maxCount;

    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int connectionTimeout;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(true); // 在借用连接时进行验证
        poolConfig.setTestOnReturn(true); // 在归还连接时进行验证
        poolConfig.setTestWhileIdle(true); // 在连接空闲时进行验证
        poolConfig.setMinEvictableIdleTimeMillis(60000); // 空闲连接的最小时间
        poolConfig.setTimeBetweenEvictionRunsMillis(30000); // 检查空闲连接的周期

        return new JedisPool(poolConfig, host, port, connectionTimeout);
    }

    @Bean
    public Integer maxCount() {
        return maxCount;
    }
}
