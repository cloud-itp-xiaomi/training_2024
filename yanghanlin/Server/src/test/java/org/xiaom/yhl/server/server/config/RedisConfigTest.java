package org.xiaom.yhl.server.server.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import static org.junit.jupiter.api.Assertions.*;
/**
 * ClassName: RedisConfigTest
 * Package: org.xiaom.yhl.server.server.config
 * Description:This is for test the redis config is not null.
 *
 * @Author 杨瀚林
 * @Create 2024/6/10 22:46
 * @Version 1.0
 */
public class RedisConfigTest {
    private RedisConfig redisConfig = new RedisConfig();
    @Test
    public void testRedisConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisConfig.redisConnectionFactory();
        assertNotNull(connectionFactory, "RedisConnectionFactory should not be null");
    }
    @Test
    public void testRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
        assertNotNull(redisTemplate, "RedisTemplate should not be null");
        assertNotNull(redisTemplate.getConnectionFactory(), "ConnectionFactory in RedisTemplate should not be null");
    }
}
