package com.example.springcloud.base;

import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.service.CollectorService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CollectMsg
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 15:58
 **/
@Component
public class CollectMsgJob implements Job {
    @Autowired
    private CollectorService collectorService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        CollectorRequest cpuRequest = collectorService.collectCpuMsg();
        collectorService.sendCollector(cpuRequest);
        CollectorRequest memRequest = collectorService.collectMemMsg();
        collectorService.sendCollector(memRequest);
    }
}
