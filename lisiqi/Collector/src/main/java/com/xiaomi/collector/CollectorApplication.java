package com.xiaomi.collector;

import com.xiaomi.collector.common.Result;
import com.xiaomi.collector.entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class CollectorApplication {

    @Autowired
    private RestTemplate restTemplate;
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Scheduled(fixedRate = 5000)
    public void uploadMetrics() {
        System.out.println("****************************************\n");

        // 模拟采集CPU和内存利用率数据
        List<Metric> metrics = new ArrayList<>();
        // 假设获取了CPU利用率和内存利用率的值
        Metric cpuMetric = new Metric("cpu.used.percent", "my-computer", System.currentTimeMillis() / 1000, 60, 60.1);
        Metric memMetric = new Metric("mem.used.percent", "my-computer", System.currentTimeMillis() / 1000, 60, 35.0);
        metrics.add(cpuMetric);
        metrics.add(memMetric);

        // 发送HTTP POST请求上传数据
        String serverUrl = "http://localhost:9092/api/metric/upload";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Metric>> request = new HttpEntity<>(metrics, headers);
        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(serverUrl, request, Result.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Metrics uploaded successfully");
        } else {
            System.out.println("Failed to upload metrics");
        }
    }


}
