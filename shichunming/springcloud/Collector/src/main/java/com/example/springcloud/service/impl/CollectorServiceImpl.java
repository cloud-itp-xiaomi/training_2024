package com.example.springcloud.service.impl;

import com.example.springcloud.base.CfgConfig;
import com.example.springcloud.base.Enums.MetricEnum;
import com.example.springcloud.base.SystemInfoUtils;
import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.controller.request.LogUploadRequest;
import com.example.springcloud.service.CollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private CfgConfig cfgConfig;
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

    @Override
    public List<LogUploadRequest> getLogRequest() {
        String hostName = systemInfoUtils.getHostName();
        List<LogUploadRequest> requestList = new ArrayList<>();
        if (Objects.isNull(cfgConfig.getFiles())){
            return requestList;
        }
        for (String file : cfgConfig.getFiles()){
            LogUploadRequest request = new LogUploadRequest();
            request.setHostName(hostName);
            request.setFile(file);
            requestList.add(request);
        }
        return requestList;
    }

    @Override
    public void logUpload(List<LogUploadRequest> requestList) {
        if (Objects.isNull(requestList)){
            return;
        }
        for (LogUploadRequest request : requestList){
            String postUrl = url;
            restTemplate.postForObject(postUrl,request,String.class);
            log.info("-------------------发送成功: Metric:"+request.getFile()+request);
        }
    }
}
