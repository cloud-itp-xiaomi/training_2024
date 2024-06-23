package com.example.springcloud.controller;

import com.example.springcloud.base.BaseJsonUtils;
import com.example.springcloud.base.Response;
import com.example.springcloud.base.RestBusinessTemplate;
import com.example.springcloud.controller.request.LogQueryRequest;
import com.example.springcloud.controller.request.LogUploadRequest;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.response.LogQueryResponse;
import com.example.springcloud.service.ServerLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName ServerLogController
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-22 23:38
 **/
@RestController
@Slf4j
@RefreshScope
@RequestMapping("/log")
public class ServerLogController {
    @Autowired
    private ServerLogService serverLogService;
    @PostMapping("/upload")
    public Response<Void> logUpload(@RequestBody LogUploadRequest request) {
        return RestBusinessTemplate.execute(() -> {
            if (StringUtils.isBlank(request.getHostName())){
                return null;
            }
            if (StringUtils.isBlank(request.getFile())){
                return null;
            }
            if (Objects.isNull(request.getLogs()) || request.getLogs().isEmpty()){
                return null;
            }
            serverLogService.logUpload(request);
            return null;
        });
    }

    @GetMapping("/query")
    public Response<LogQueryResponse> logQuery(@RequestParam(value = "hostname", required = false) String hostName,
                                                     @RequestParam(value = "file", required = false) String file) {
        return RestBusinessTemplate.execute(() -> {
            log.info("logQuery Request :hostName:{},file:{}", hostName, file);
            LogQueryRequest request = new LogQueryRequest();
            if (StringUtils.isBlank(hostName)){
                return null;
            }
            if (StringUtils.isBlank(file)){
                return null;
            }
            request.setHostName(hostName);
            request.setFile(file);
            LogQueryResponse responses = serverLogService.queryLog(request);
            log.info("\n--------------------------------\n" +
                    "logQuery Response:{}", BaseJsonUtils.writeValue(responses));
            return responses;
        });
    }
}
