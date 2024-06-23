package com.example.springcloud.controller;

import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.controller.request.LogUploadRequest;
import com.example.springcloud.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName CollectorController
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 00:42
 **/
@RestController
@RequestMapping("/collector")
public class CollectorController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CollectorService collectorService;

//    @Scheduled(cron = "* 0/5 *  * * ? ")   //每5秒执行一次0/5 * *  * * ? || 每5分钟执行一次0 */5 * * * * ?
    public void sendTask() {
        CollectorRequest cpuRequest = collectorService.collectCpuMsg();
        collectorService.sendCollector(cpuRequest);
        CollectorRequest memRequest = collectorService.collectMemMsg();
        collectorService.sendCollector(memRequest);
    }

    @GetMapping("/log")
    public void sendLog() {
        List<LogUploadRequest> requestList = collectorService.getLogRequest();
        collectorService.logUpload(requestList);
    }
}
