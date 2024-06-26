package org.qiaojingjing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.entity.Log;
import org.qiaojingjing.entity.Metric;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class POSTRequestUtil {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public static <T> void sendPostRequest(String url, T[] items, Class<T[]> responseType) {
        try {
            String json = objectMapper.writeValueAsString(items);
            log.info("Sending JSON: {}", json);

            HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Response: {}", responseEntity.getBody());
            } else {
                log.warn("发送请求失败 Status code: {}, Response body: {}",
                        responseEntity.getStatusCode(),
                        responseEntity.getBody());
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON", e);
        }
    }

    public static void sendMetricsPostRequest(Metric[] metrics) {
        sendPostRequest("http://server1:8080/api/metric/upload", metrics, Metric[].class);
    }

    public static void sendLogsPostRequest(Log[] logs) {
        sendPostRequest("http://localhost:8080/api/log/upload", logs, Log[].class);
    }
}