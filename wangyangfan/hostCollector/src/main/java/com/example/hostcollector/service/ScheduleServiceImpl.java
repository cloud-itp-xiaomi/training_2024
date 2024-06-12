package com.example.hostcollector.service;

import com.example.hostcollector.pojo.UploadData;
import com.example.hostcollector.utils.IOProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * @Author: WangYF
 * @Date: 2024/05/28
 * @Description: 定时任务服务，一分钟发一次Http请求
 */
@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${collection.trans-url}")
    private String transUrl;

    @Value("${collection.cpu-metric-name}")
    private String cpuMetricName;

    @Value("${collection.mem-metric-name}")
    private String memMetricName;

    @Value("${collection.delay-seconds}")
    private Long step;

    @Scheduled(cron = "${collection.cron}")
    @Override
    public void timedCollection() throws IOException {

        String hostName = InetAddress.getLocalHost().getHostName();
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        ArrayList<UploadData> data = new ArrayList<>();
        data.add(new UploadData(cpuMetricName, hostName, currentTimeSeconds, step, IOProcessUtils.getCPUUtilization()));
        data.add(new UploadData(memMetricName, hostName, currentTimeSeconds, step, IOProcessUtils.getMemUtilization()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ArrayList<UploadData>> req = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(transUrl, req, String.class);
        Assert.notNull(responseEntity.getBody(), "Response Body is Null");
    }
}
