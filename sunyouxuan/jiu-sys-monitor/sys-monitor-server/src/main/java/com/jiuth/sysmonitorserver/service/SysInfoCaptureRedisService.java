package com.jiuth.sysmonitorserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorserver.dto.SysInfoCaptureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public interface SysInfoCaptureRedisService {


    void cacheCpuUsage(SysInfoCaptureDTO sysInfo);

    void cacheMemUsage(SysInfoCaptureDTO sysInfo);

    void cacheUsage(String key, SysInfoCaptureDTO sysInfo);
}