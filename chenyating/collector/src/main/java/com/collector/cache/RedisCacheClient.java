package com.collector.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

// 主要功能就前两个，保存和获取
@Component
public class RedisCacheClient implements CacheClient{
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void saveLatestData(String key, String value) {
        // 保存0-8，九条，添加新数据到列表头部
        redisTemplate.opsForList().trim(key,0,8);
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public List<Object> getLatestData(String key) {
        // 从第一个到最后一个，获取全部
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
