package com.example.springcloud.controller;

import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CollectorController
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 00:42
 **/
@RestController
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
}
