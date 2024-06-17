package com.jiuth.sysmonitorcapture.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorcapture.config.LogMonitorConfig;
import com.jiuth.sysmonitorcapture.storage.LogStorageFactory;
import com.jiuth.sysmonitorcapture.storage.LogStorageService;
import com.jiuth.sysmonitorcapture.util.CommonUtil;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LogMonitorService {

    @Value("${serverl.api.base}")
    private String serverUrl;

    @Value("${serverl.api.log}")
    private String api;

    private final LogMonitorConfig.Config logMonitorConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final Map<String, List<String>> fileLogs = new ConcurrentHashMap<>();
    private final AtomicInteger dataCounter = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> sendJsonTask;
    private LogStorageService logStorageService;

    @Autowired
    private LogStorageFactory logStorageFactory;

    @Autowired
    public LogMonitorService(LogMonitorConfig.Config logMonitorConfig, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.logMonitorConfig = logMonitorConfig;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;


    }

    @PostConstruct
    public void init() throws Exception {
        this.logStorageService =  logStorageFactory.createLogStorageService(logMonitorConfig.getLogStorage());
        List<String> files = logMonitorConfig.getFiles();
        for (String file : files) {
            startTailer(file);
        }
    }

    private void startTailer(String filePath) throws Exception {
        File file = new File(filePath);
        Tailer tailer = new Tailer(file, new MyTailerListener(filePath), 1000, true);

        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
    }

    private class MyTailerListener extends TailerListenerAdapter {
        private final String filePath;

        public MyTailerListener(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void handle(String line) {
            fileLogs.computeIfAbsent(filePath, k -> new ArrayList<>()).add(line);
            dataCounter.incrementAndGet();

            // 如果已经有发送任务在执行，则取消当前计划，重新计划发送任务
            if (sendJsonTask != null && !sendJsonTask.isDone()) {
                sendJsonTask.cancel(false);
            }
            scheduleSendJsonTask();
        }

        private void scheduleSendJsonTask() {
            sendJsonTask = scheduler.schedule(() -> {
                try {
                    if (dataCounter.get() >= logMonitorConfig.getFiles().size()) {

                        sendJsonData();
                        logStorageService.storeLogs(fileLogs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dataCounter.set(0);
                    fileLogs.clear();
                }
            }, 1, TimeUnit.SECONDS);
        }

        private void sendJsonData() throws Exception {
            String json = createJson();
            System.out.println(json);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl+api, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("JSON data sent successfully.");
            } else {
                System.out.println("Failed to send JSON data. Response code: " + response.getStatusCode());
            }
        }

        private String createJson() throws JsonProcessingException {
            List<Map<String, Object>> logEntries = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : fileLogs.entrySet()) {
                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("hostname", CommonUtil.initializeEndpoint(serverUrl));
                logEntry.put("file", entry.getKey());
                logEntry.put("logs", new ArrayList<>(entry.getValue()));
                logEntries.add(logEntry);
            }

            String json = objectMapper.writeValueAsString(logEntries);
            return json;
        }
    }


    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }
}

