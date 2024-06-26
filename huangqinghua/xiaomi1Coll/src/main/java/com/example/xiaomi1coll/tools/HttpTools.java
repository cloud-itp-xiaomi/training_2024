package com.example.xiaomi1coll.tools;

import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpTools 是一个用于发送带有 JSON 负载的 HTTP POST 请求的工具类。
 * 使用 Spring 的 RestTemplate 来执行 HTTP 请求，并提供对请求和响应的日志记录。
 */
public class HttpTools {

    private static final Logger logger = LoggerFactory.getLogger(HttpTools.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * @param url  要发送 POST 请求的 URL
     * @param body 请求的主体，将被序列化为 JSON
     * @param <T>  请求主体的类型
     * @return 包含响应状态和主体的响应实体
     * @throws ResourceAccessException 请求超时
     * @throws Exception 发送请求时发生任何其他错误
     */
    public static <T> ResponseEntity<String> sendPostRequest(String url, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            logger.info("Request to {} sent successfully.", url);
            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());
            return response;
        } catch (ResourceAccessException e) {
            logger.error("Request timeout when sending request to {}: {}", url, body, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error sending request to {}: {}", url, body, e);
            throw e;
        }
    }
}
