package com.jiuth.sysmonitorserver.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorserver.dto.SysInfoCaptureDTO;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysInfoCaptureRedisServiceImpl implements SysInfoCaptureRedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String CPU_KEY = "cpu_used_percent";
    private static final String MEM_KEY = "mem_used_percent";
    private static final int LIMIT = 10;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void cacheCpuUsage(SysInfoCaptureDTO sysInfo) {
        cacheUsage(CPU_KEY, sysInfo);
    }

    @Override
    public void cacheMemUsage(SysInfoCaptureDTO sysInfo) {
        cacheUsage(MEM_KEY, sysInfo);
    }

    @Override
    public void cacheUsage(String key, SysInfoCaptureDTO sysInfo) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            String json = objectMapper.writeValueAsString(sysInfo);
            listOps.leftPush(key, json);
            if (listOps.size(key) > LIMIT) {
                listOps.rightPop(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SysInfoCaptureDTO> getCpuUsage() {
        return getUsage(CPU_KEY);
    }

    public List<SysInfoCaptureDTO> getMemUsage() {
        return getUsage(MEM_KEY);
    }

    private List<SysInfoCaptureDTO> getUsage(String key) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            List<String> jsonList = listOps.range(key, 0, -1);
            return jsonList.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, SysInfoCaptureDTO.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}