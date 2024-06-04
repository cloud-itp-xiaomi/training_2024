package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.mapper.MetricMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    @Autowired
    private MetricMapper metricMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveMetric(Metric metric) {
        metricMapper.insert(metric);

         // 使用Redis列表存储最近10条数据
//        redisTemplate.opsForList().leftPush("metrics", metric);
//        if (redisTemplate.opsForList().size("metrics") > 10) {
//            redisTemplate.opsForList().trim("metrics", 0, 9);
//        }
    }
}
