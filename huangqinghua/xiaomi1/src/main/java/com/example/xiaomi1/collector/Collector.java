package com.example.xiaomi1.collector;

import com.example.xiaomi1.entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.Instant;

@Component
public class Collector {

    private final RestTemplate restTemplate;
    private final OperatingSystemMXBean osBean;

    public Collector() {
        this.restTemplate = new RestTemplate();
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
    }

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void collectAndReport() {
        double cpuLoad = osBean.getSystemLoadAverage(); // 获取系统的平均负载（过去1分钟）
        double memoryUsage = getMemoryUsage(); // 获取内存使用情况

        Metric metric = new Metric();
        metric.setComName("MyComputer");
        metric.setCpuUsedPercent(cpuLoad);
        metric.setMemUsedPercent(memoryUsage);
        metric.setTimeStamp(Instant.now().getEpochSecond()); // 获取当前时间戳

        // 构造HTTP请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Metric> requestEntity = new HttpEntity<>(metric, headers);


        // 发送POST请求到Server的API接口
        restTemplate.exchange("http://localhost:8080/api/metric/upload", HttpMethod.POST, requestEntity, Void.class);
    }

    private double getMemoryUsage() {
        // 这里可以使用 Runtime 或者其他方式获取内存使用率
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (double) (totalMemory - freeMemory) / totalMemory * 100;
    }
}
