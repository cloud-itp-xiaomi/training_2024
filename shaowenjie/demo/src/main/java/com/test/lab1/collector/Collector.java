package com.test.lab1.collector;

import com.test.lab1.entity.Metric;
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

    @Scheduled(fixedRate = 60000)
    public void collectAndReport() {
        double cpuUsage = osBean.getSystemLoadAverage();
        double memoryUsage = getMemoryUsage();

        Metric metric = new Metric();
        metric.setMetric("mem.used.percent");
        metric.setEndpoint("my-computer");
        metric.setTimestamp(Instant.now().getEpochSecond());
        metric.setStep(60);
        metric.setValue(memoryUsage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Metric> requestEntity = new HttpEntity<>(metric, headers);

        restTemplate.exchange("http://localhost:8080/api/metric/upload", HttpMethod.POST, requestEntity, Void.class);
    }

    private double getMemoryUsage() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (double) (totalMemory - freeMemory) / totalMemory * 100;
    }
}
