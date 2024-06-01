package org.qiaojingjing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qiaojingjing.entity.Metric;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class POSTRequest {
    static RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void sendPostRequest(Metric[] metrics){

        try {
            //设置请求头为 application/json
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

            //转json
            String json = objectMapper.writeValueAsString(metrics);
            System.out.println(json);

            //组装请求信息
            HttpEntity<String> httpEntity = new HttpEntity<>(json,headers);
            String response = restTemplate.postForObject("http://localhost:8080/api/metric/upload", httpEntity, String.class);
            System.out.println("Response:"+response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
