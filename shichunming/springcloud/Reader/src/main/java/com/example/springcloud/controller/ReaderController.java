package com.example.springcloud.controller;


import cn.hutool.json.JSONUtil;
import com.example.springcloud.controller.base.Enum.MetricEnum;
import com.example.springcloud.controller.base.Response;
import com.example.springcloud.controller.request.ReaderRequest;
import com.example.springcloud.controller.response.ReaderResponse;
import com.example.springcloud.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ReaderController
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 17:06
 **/
@Slf4j
@RestController
public class ReaderController {
    @Autowired
    private ReaderService readerService;

    public Response<ReaderResponse> readMsg(String endpoint, Integer metricCode, Long start_ts, Long end_ts) {
        ReaderRequest request = new ReaderRequest();
        request.setEndpoint(endpoint);
        request.setMetric(MetricEnum.getName(metricCode));
        request.setStart_ts(start_ts);
        request.setEnd_ts(end_ts);
        String jsonStr = JSONUtil.toJsonStr(request);
        log.info("\n-----------------------\n 发送请求数据 request:{}", jsonStr);
        return readerService.readerMsg(request);
    }
}
