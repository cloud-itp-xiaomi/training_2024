package com.xiaomi.collector;

import com.sun.management.OperatingSystemMXBean;

import com.xiaomi.collector.common.Result;
import com.xiaomi.collector.entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class CollectorApplication {

    private final long FixedRated=5000;

    @Autowired
    private RestTemplate restTemplate;
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Scheduled(fixedRate = FixedRated)
    public void uploadMetrics() {

        // 模拟采集CPU和内存利用率数据
        List<Metric> metrics = new ArrayList<>();

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getSystemCpuLoad() * 100;
        double memLoad = (1 - (double) osBean.getFreePhysicalMemorySize() / osBean.getTotalPhysicalMemorySize()) * 100;
        long timestamp = System.currentTimeMillis() / 1000;
        long step=FixedRated/1000;

        Metric cpuMetric = new Metric("cpu.used.percent", "my-computer", timestamp, step, cpuLoad);
        Metric memMetric = new Metric("mem.used.percent", "my-computer", timestamp, step, memLoad);
        /*System.out.println(cpuMetric);
        System.out.println(cpuMetric);
        System.out.println("************************************\n");*/

        /*
        // 测试用的假数据，假设获取了CPU利用率和内存利用率的值
        Metric cpuMetric = new Metric("cpu.used.percent", "my-computer", System.currentTimeMillis() / 1000, 60, 60.1);
        Metric memMetric = new Metric("mem.used.percent", "my-computer", System.currentTimeMillis() / 1000, 60, 35.0);*/

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
