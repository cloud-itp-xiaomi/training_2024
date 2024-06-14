package com.example.mi1.service;


import com.example.mi1.domain.UploadParam;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SaveNewestDataService {
    void saveData(String redisKey, Object message);
    <T> List<T> queryData(String redisKey, Class<T> clazz);
    List<UploadParam> rangeQuery(String redisKey, String endpoint, long startTimestamp, long endTimestamp);
}
