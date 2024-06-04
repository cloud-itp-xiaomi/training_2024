package com.jiuth.sysmonitorcapture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorcapture.util.OSVersionUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;



@Component
public class MetricCollector {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${interval}")
    private long interval;

    @Value("${cpu.stat.file.path}")
    private String cpuStatFilePath;

    @Value("${meminfo.file.path}")
    private String meminfoFilePath;

    private final RestTemplate restTemplate;
    private long previousIdleTime = 0, previousTotalTime = 0;
    private final NumberFormat percentFormatter;
    private final String STAT_FILE_HEADER = "cpu  ";

    private static String endpoint= System.getProperty("user.name", "UnknownUser")+"@"+OSVersionUtil.getLinuxDistribution();;

    public MetricCollector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.percentFormatter = NumberFormat.getPercentInstance();
        percentFormatter.setMaximumFractionDigits(2);
    }

//    @PostConstruct
//    public void init() {
//        // 初始化完成后执行任务
//        captureMetrics();
//    }

    @Scheduled(fixedRateString = "${interval}")
    public void captureMetrics() {
        double cpuUtilization = getCpuUtilization();
        double memUtilization = getMemUtilization();
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> cpuMetric = createMetric("cpu.used.percent", cpuUtilization, timestamp);
        Map<String, Object> memMetric = createMetric("mem.used.percent", memUtilization, timestamp);

        // 输出详细的 CPU 和内存信息
        System.out.println("CPU Utilization: " + percentFormatter.format(cpuUtilization / 100));
        System.out.println("Memory Utilization: " + percentFormatter.format(memUtilization / 100));

        // 准备指标数组
        Map<String, Object>[] metricsArray = new Map[]{cpuMetric, memMetric};

        // 打印发送的 JSON 数据
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(metricsArray);
            System.out.println("Sending JSON: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 发送指标到服务器，并打印返回的 JSON 数据
        String response = restTemplate.postForObject(serverUrl, metricsArray, String.class);
        System.out.println("Server Response JSON: " + response);
    }

    private double getCpuUtilization() {
        try (RandomAccessFile statFile = new RandomAccessFile(cpuStatFilePath, "r")) {
            String[] values = statFile.readLine().substring(STAT_FILE_HEADER.length()).split("\\s+");

            long idleTime = Long.parseLong(values[3]);
            long totalTime = 0;
            for (String value : values) {
                totalTime += Long.parseLong(value);
            }

            long idleTimeDelta = idleTime - previousIdleTime;
            long totalTimeDelta = totalTime - previousTotalTime;
            double utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

            previousIdleTime = idleTime;
            previousTotalTime = totalTime;

            return utilization * 100;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private double getMemUtilization() {
        try (BufferedReader meminfoReader = new BufferedReader(new FileReader(meminfoFilePath))) {
            String line;
            long totalMemory = 0;
            long availableMemory = 0;

            while ((line = meminfoReader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    totalMemory = parseMemoryValue(line);
                } else if (line.startsWith("MemAvailable:")) {
                    availableMemory = parseMemoryValue(line);
                }
            }

            long usedMemory = totalMemory - availableMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            // 输出详细的内存信息
            System.out.println("Total Memory: " + totalMemory + " KB");
            System.out.println("Used Memory: " + usedMemory + " KB");

            return memoryUsage;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long parseMemoryValue(String line) {
        String[] parts = line.split("\\s+");
        return Long.parseLong(parts[1]);
    }

    private Map<String, Object> createMetric(String metricName, double value, long timestamp) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("metric", metricName);
        metric.put("endpoint", endpoint);
        metric.put("timestamp", timestamp);
        metric.put("step", 60);
        metric.put("value", value);
        return metric;
    }

//    private String getEndpoint() {
//        String username = System.getProperty("user.name", "UnknownUser");
//        String osType = System.getProperty("os.name", "UnknownOS");
//
//        return username + "@" + osType;
//    }
}