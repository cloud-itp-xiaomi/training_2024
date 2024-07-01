package org.xiaom.yhl.collector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: MetricsUploader
 * Package: org.xiaom.yhl.collector.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/31 14:46
 * @Version 1.0
 */
@Service
public class MetricsUploader {

    private final RestTemplate restTemplate;

    @Autowired
    public MetricsUploader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void uploadMetric(String metric, double value) {
        String url = "http://localhost:8080/api/metric/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> metricData = new HashMap<>();
        metricData.put("metric", metric);
        metricData.put("endpoint", "my-computer");
        metricData.put("timestamp", System.currentTimeMillis() / 1000);
        metricData.put("step", 60);
        metricData.put("value", value);

        Map<String, Object>[] metricArray = new Map[]{metricData};

        HttpEntity<Map<String, Object>[]> request = new HttpEntity<>(metricArray, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}