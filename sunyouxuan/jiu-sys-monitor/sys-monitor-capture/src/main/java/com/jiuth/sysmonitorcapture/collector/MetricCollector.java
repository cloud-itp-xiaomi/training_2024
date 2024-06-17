package com.jiuth.sysmonitorcapture.collector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorcapture.util.IpUtil;
import com.jiuth.sysmonitorcapture.util.OSVersionUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MetricCollector {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${interval}")
    private long interval;

    private String endpoint;

    private final RestTemplate restTemplate;
    private final CpuUtilizationCollector cpuUtilizationCollector;
    private final MemoryUtilizationCollector memoryUtilizationCollector;


    public MetricCollector(RestTemplate restTemplate, CpuUtilizationCollector cpuUtilizationCollector,
                           MemoryUtilizationCollector memoryUtilizationCollector) {
        this.restTemplate = restTemplate;
        this.cpuUtilizationCollector = cpuUtilizationCollector;
        this.memoryUtilizationCollector = memoryUtilizationCollector;
    }

    /**
     * 防止依赖注入导致初始化时serverUrl没有值
     * 初始化 endpoint
     */
    @PostConstruct
    private void initialize() {
        endpoint = initializeEndpoint();
    }

    @Scheduled(fixedRateString = "${interval}")
    public void captureMetrics() {
        double cpuUtilization = cpuUtilizationCollector.getCpuUtilization();
        double memUtilization = memoryUtilizationCollector.getMemUtilization();
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> cpuMetric = createMetric("cpu.used.percent", cpuUtilization, timestamp);
        Map<String, Object> memMetric = createMetric("mem.used.percent", memUtilization, timestamp);

        // 打印详细的 CPU 和内存信息
        log.info("CPU 使用率: {}", cpuUtilization);
        log.info("内存使用率: {}", memUtilization);

        // 准备指标数组
        Map<String, Object>[] metricsArray = new Map[]{cpuMetric, memMetric};

        // 将指标数组转换为 JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(metricsArray);
            log.info("发送 JSON: {}", json);
        } catch (JsonProcessingException e) {
            log.error("转换 JSON 出错: {}", e.getMessage());
        }

        // 发送指标到服务器
        try {
            String response = restTemplate.postForObject(serverUrl, metricsArray, String.class);
            log.info("服务器响应 JSON: {}", response);
        } catch (Exception e) {
            log.error("发送指标到服务器出错: {}", e.getMessage());
        }
    }

    private String initializeEndpoint() {
        String username = System.getProperty("user.name", "UnknownUser");
        String systemVersion = OSVersionUtil.getSystemVersion();
        String ipAddress = null;
        if (serverUrl != null && (serverUrl.contains("127.0.0.1") || serverUrl.contains("http://localhost"))) {
            ipAddress = "127.0.0.1";
        } else {
            ipAddress = IpUtil.getLocalIpAddress();
        }

        return username + "@" + systemVersion + "@" + ipAddress;
    }


    private Map<String, Object> createMetric(String metricName, double value, long timestamp) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("metric", metricName);
        metric.put("endpoint", endpoint);
        metric.put("timestamp", timestamp);
        metric.put("step", interval/1000);
        metric.put("value", value);
        return metric;
    }
}