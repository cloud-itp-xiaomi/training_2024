package com.example.hostcollector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: WangYF
 * @Date: 2024/05/28
 * @Description: 定时任务服务，一分钟发一次Http请求
 */
@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 * * * * ?")
    @Override
    public void timedCollection() {
        System.out.println("hello");
    }
}
