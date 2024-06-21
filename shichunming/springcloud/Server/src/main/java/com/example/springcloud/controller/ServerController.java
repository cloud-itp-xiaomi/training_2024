package com.example.springcloud.controller;

import com.example.springcloud.base.BaseJsonUtils;
import com.example.springcloud.base.MyException;
import com.example.springcloud.base.Response;
import com.example.springcloud.base.RestBusinessTemplate;
import com.example.springcloud.base.enums.ErrorCode;
import com.example.springcloud.base.enums.MetricEnum;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.response.MetricQueryResponse;
import com.example.springcloud.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


/**
 * @ClassName CollectorController
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:20
 **/
@RestController
@Slf4j
@RefreshScope
@RequestMapping("/metric")
public class ServerController {
    @Autowired
    private ServerService serverService;
    @Value("${pattern.dateformat}")
    private String dateFormat;
    @RequestMapping("/test")
    public String test() {
        log.info("test你好");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat));
    }
    @PostMapping("/upload")
    public Response<Void> metricUpload(@RequestBody MetricUploadRequest request) {
        return RestBusinessTemplate.execute(() -> {
            log.info("\n-------------------------------------\n" +
                    "接收成功 Request:{}", BaseJsonUtils.writeValue(request));
            if (Objects.isNull(request)) {
                return null;
            }
            if (MetricEnum.check(request.getMetric())) {
                throw new MyException(ErrorCode.PARAM_ERROR, "metric is null");
            }
            if (StringUtils.isBlank(request.getEndpoint())) {
                throw new MyException(ErrorCode.PARAM_ERROR, "endpoint is null");
            }
            serverService.metricUpload(request);
            return null;
        });
    }

    @PostMapping("/query")
    public Response<List<MetricQueryResponse>> metricQuery(@RequestBody MetricQueryRequest request) {
        return RestBusinessTemplate.execute(() -> {
            log.info("\n-----------------------------\n" +
                    "metricQuery Request:{}", BaseJsonUtils.writeValue(request));
            List<MetricQueryResponse> response = serverService.queryMetric(request);
            log.info("\n--------------------------------\n" +
                    "metricQuery Response:{}", BaseJsonUtils.writeValue(response));
            return response;
        });
    }
}
