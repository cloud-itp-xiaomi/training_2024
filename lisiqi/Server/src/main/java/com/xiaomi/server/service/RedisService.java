package com.xiaomi.server.service;

import com.xiaomi.server.Entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private static final int MAX_ENTRIES = 10;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveMetric(Metric metric) {
        String key = metric.getMetric();
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.leftPush(key, metric);
        listOps.trim(key, 0, MAX_ENTRIES - 1);
    }
}
