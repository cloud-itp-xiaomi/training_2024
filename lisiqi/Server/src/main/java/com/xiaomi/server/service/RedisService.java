package com.xiaomi.server.service;

import com.xiaomi.server.Entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String METRIC_KEY_PREFIX = "metric:";

    public void saveMetric(Metric metric) {
        redisTemplate.opsForValue().set(METRIC_KEY_PREFIX + metric.getId(), metric);
    }
}
