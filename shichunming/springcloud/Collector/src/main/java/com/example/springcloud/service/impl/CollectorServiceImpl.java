package com.example.springcloud.service.impl;

import com.example.springcloud.base.Enums.MetricEnum;
import com.example.springcloud.base.SystemInfoUtils;
import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.service.CollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CollectorServiceImpl
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 00:47
 **/
@Service
@Slf4j
@RefreshScope
public class CollectorServiceImpl implements CollectorService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SystemInfoUtils systemInfoUtils;
    @Value("${schedule.quartzTime}")
    private Integer quartzTime;
    @Value("${schedule.url}")
    private String url;
    @Override
    public void sendCollector(CollectorRequest request) {
        String postUrl = url;
        restTemplate.postForObject(postUrl,request,String.class);
        log.info("-------------------发送成功: Metric:"+request.getMetric()+request);
    }

    @Override
    public CollectorRequest collectCpuMsg() {
        CollectorRequest request = new CollectorRequest();
        request.setMetric(MetricEnum.getName(1));
        request.setEndpoint(systemInfoUtils.getHostName());
        request.setStep(quartzTime);
        request.setTimestamp(System.currentTimeMillis()/1000);
        request.setValue(systemInfoUtils.getCpuInfo());
        return request;
    }

    @Override
    public CollectorRequest collectMemMsg() {
        CollectorRequest request = new CollectorRequest();
        request.setMetric(MetricEnum.getName(2));
        request.setEndpoint(systemInfoUtils.getHostName());
        request.setStep(quartzTime);
        request.setTimestamp(System.currentTimeMillis()/1000);
        request.setValue(systemInfoUtils.getMemInfo());
        return request;
    }


}
