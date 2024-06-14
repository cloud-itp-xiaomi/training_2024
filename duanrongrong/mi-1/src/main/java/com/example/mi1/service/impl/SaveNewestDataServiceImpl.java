package com.example.mi1.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.mi1.domain.UploadParam;
import com.example.mi1.service.SaveNewestDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
@Slf4j
public class SaveNewestDataServiceImpl implements SaveNewestDataService {
    /**
     * 若实现最大记录是 10，则应该为 9
     */
    private final Integer maxCount;

    private final JedisPool jedisPool;

    private final ObjectMapper objectMapper;


    @Autowired
    SaveNewestDataServiceImpl(Integer maxCount, JedisPool jedisPool, ObjectMapper objectMapper) {
        this.maxCount = maxCount;
        this.jedisPool = jedisPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveData(String redisKey, Object message) {
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(redisKey, jsonString);
            jedis.ltrim(redisKey, 0, maxCount - 1);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public <T> List<T> queryData(String redisKey, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> jsonList = jedis.lrange(redisKey, 0, -1);
            List<T> resultList = new ArrayList<>();
            if (jsonList != null) {
                for (String jsonString : jsonList) {
                    try {
                        T obj = objectMapper.readValue(jsonString, clazz);
                        resultList.add(obj);
                    } catch (JsonProcessingException e) {
                        log.info(e.getMessage());
                    }
                }
            }
            return resultList;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<UploadParam> rangeQuery(String redisKey, String endpoint, long startTimestamp, long endTimestamp) {
        List<UploadParam> results = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
        List<String> jsonList = jedis.lrange(redisKey, 0, -1); // 获取整个列表
        try {
            if (jsonList != null && jsonList.size() > 0) {
                UploadParam first = objectMapper.readValue(jsonList.get(0), UploadParam.class);
                UploadParam last = objectMapper.readValue(jsonList.get(jsonList.size() - 1), UploadParam.class);
                if (first.getTimestamp() < endTimestamp || last.getTimestamp() > startTimestamp)
                    return null;
                for (String jsonString : jsonList) {
                    UploadParam obj = objectMapper.readValue(jsonString, UploadParam.class);
                    if (endpoint.equals(obj.getEndpoint()) && obj.getTimestamp() >= startTimestamp && obj.getTimestamp() <= endTimestamp) {
                        results.add(obj);
                    }
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); // 处理反序列化异常
        }
        return results;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
