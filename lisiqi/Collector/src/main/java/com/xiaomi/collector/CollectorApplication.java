package com.xiaomi.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.management.OperatingSystemMXBean;

import com.xiaomi.collector.common.Result;
import com.xiaomi.collector.config.CFGConfig;
import com.xiaomi.collector.entity.LogEntry;
import com.xiaomi.collector.entity.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class CollectorApplication {

    private final long FixedRated=5000;

    @Autowired
    private RestTemplate restTemplate;

    private static CFGConfig cfgconfig;

    public static void main(String[] args) throws IOException{
        //SpringApplication.run(CollectorApplication.class, args);
        initializeCollector("D:\\2024_training\\Collector\\cfg.json");
    }

    @Scheduled(fixedRate = FixedRated)
    public void uploadMetrics() {

        // 采集CPU和内存利用率数据
        List<Metric> metrics = new ArrayList<>();

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getSystemCpuLoad() * 100;
        double memLoad = (1 - (double) osBean.getFreePhysicalMemorySize() / osBean.getTotalPhysicalMemorySize()) * 100;
        long timestamp = System.currentTimeMillis() / 1000;
        long step=FixedRated/1000;

        Metric cpuMetric = new Metric("cpu.used.percent", "my-computer", timestamp, step, cpuLoad);
        Metric memMetric = new Metric("mem.used.percent", "my-computer", timestamp, step, memLoad);

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

    public static void initializeCollector(String configFilePath) throws IOException {
        readConfig(configFilePath);
        startFileWatchers();
    }

    //读取cfg配置文件
    private static void readConfig(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        cfgconfig = mapper.readValue(Paths.get(configFilePath).toFile(), CFGConfig.class);
    }

    //启动文件监视器,对每个log都开新线程监视
    private static void startFileWatchers() throws IOException {
        for (String filePath : cfgconfig.getFiles()) {
            Path path = Paths.get(filePath);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            new Thread(() -> {
                try {
                    watchFileChanges(watchService, path);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    //无限循环观察文件变化，上传日志
    private static void watchFileChanges(WatchService watchService, Path path) throws IOException, InterruptedException {
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    if (path.endsWith(event.context().toString())) {
                        List<String> lines = Files.readAllLines(path);
                        uploadLogs(path.toString(), lines);
                    }
                }
            }
            key.reset();
        }
    }

    private static void uploadLogs(String filePath, List<String> logs) {
        List<LogEntry> logEntries = Collections.singletonList(
                new LogEntry("my-computer", filePath, logs)
        );

        // 添加 log_storage 配置到请求中
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("log_storage", cfgconfig.getLogStorage());
        requestBody.put("logs", logEntries);

        RestTemplate restTemplate = new RestTemplate();
        String serverUrl = "http://localhost:9092/api/log/upload";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(serverUrl, request, Result.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Logs uploaded successfully");
        } else {
            System.out.println("Failed to upload logs: " + responseEntity.getStatusCode());
        }
    }


}
