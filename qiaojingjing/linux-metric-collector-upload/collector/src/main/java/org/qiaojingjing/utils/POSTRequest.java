package org.qiaojingjing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qiaojingjing.entity.Metric;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpConnectTimeoutException;

public class POSTRequest {
    static RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendPostRequest(Metric[] metrics) {

        try {
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

            String json = objectMapper.writeValueAsString(metrics);
            System.out.println(json);

            HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
            String response = restTemplate.postForObject("http://server1:8080/api/metric/upload",
                                                              httpEntity,
                                                              String.class);
            System.out.println("Response:" + response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
