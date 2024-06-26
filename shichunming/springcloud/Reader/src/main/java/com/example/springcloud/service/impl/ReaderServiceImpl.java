package com.example.springcloud.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.springcloud.controller.base.Response;
import com.example.springcloud.controller.request.ReaderRequest;
import com.example.springcloud.controller.response.LogQueryResponse;
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
    @Value("${schedule.metricUrl}")
    private String metricUrl;

    @Value("${schedule.logUrl}")
    private String logUrl;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public Response<ReaderResponse> readerMsg(ReaderRequest request) {
        String getUrl = metricUrl;
        String response = restTemplate.postForObject(getUrl, request, String.class);
        log.info("\n--------------------------------\n 收到请求数据 response:{}",response);
        if (response != null) {
            return JSONUtil.toBean(response, Response.class);
        }
        return null;
    }

    @Override
    public Response<LogQueryResponse> queryLog(String hostname, String file) {
        String getUrl = logUrl;
        if (hostname == null || file == null) {
            return new Response<>(false, 500, "请求参数错误", null);
        }
        getUrl = getUrl + "?hostname=" + hostname + "&file=" + file;
        String response = restTemplate.getForObject(getUrl, String.class);
        log.info("\n--------------------------------\n 收到请求数据 response:{}",response);
        if (response != null) {
            return JSONUtil.toBean(response, Response.class);
        }
        return new Response<>(false, 500, "返回参数为空", null);
    }


}
