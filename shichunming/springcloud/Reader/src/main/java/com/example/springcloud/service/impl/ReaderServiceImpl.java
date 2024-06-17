package com.example.springcloud.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.springcloud.controller.base.Response;
import com.example.springcloud.controller.request.ReaderRequest;
import com.example.springcloud.controller.response.ReaderResponse;
import com.example.springcloud.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName ReaderServiceImpl
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 17:15
 **/
@Service
@Slf4j
@RefreshScope
public class ReaderServiceImpl implements ReaderService {
    @Value("${schedule.url}")
    private String url;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public Response<ReaderResponse> readerMsg(ReaderRequest request) {
        String getUrl = url;
        String response = restTemplate.postForObject(getUrl, request, String.class);
        log.info("\n--------------------------------\n 收到请求数据 response:{}",response);
        return JSONUtil.toBean(response, Response.class);
    }


}
