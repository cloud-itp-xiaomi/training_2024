package com.h_c.collector.service;

import com.h_c.collector.entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//upload
@Service
@EnableScheduling
public class MetricCollectorService {
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    public CollectorService collectorService;

    public MetricCollectorService() {
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 60000)
    public void collectAndSendMetrics() {

        double cpuUsage = collectorService.getCpuUsage();
        double memUsage = collectorService.getMemUsage();

        Metric cpuMetric = new Metric("cpu.used.percent", "my-computer", System.currentTimeMillis(), 60, cpuUsage);
        Metric memMetric = new Metric("mem.used.percent", "my-computer", System.currentTimeMillis(), 60, memUsage);

        List<Metric> metrics = Arrays.asList(cpuMetric, memMetric);
        restTemplate.postForEntity("http://localhost:8081/api/metric/upload", metrics, String.class);
    }

}
