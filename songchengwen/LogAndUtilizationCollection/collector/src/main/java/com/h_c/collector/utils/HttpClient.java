package com.h_c.collector.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
    private static final String server_url = "http://localhost:8081/api/log/upload";

    public static void sendLogs(String filePath, List<String> logs, String logStorage) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> request = new HashMap<>();
        request.put("hostname", getHostName()); //my-computer
        request.put("file", filePath);
        request.put("logs", logs);
        request.put("logStorage", logStorage);
        System.out.println("验证是否发送正确日志数据：主机名: "+getHostName()+"; 日志路径:"+filePath+"; 日志内容:"+logs+"; 所选存储方式："+logStorage);

        ResponseEntity<String> response = restTemplate.postForEntity(server_url, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to send logs to server: " + response.getBody());
        }
    }

    private static String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown-host";
        }
    }
}
