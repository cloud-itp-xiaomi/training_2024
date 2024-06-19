package com.txh.xiaomi2024.work.collector.job;


import com.txh.xiaomi2024.work.UtilizationUploadService;
import com.txh.xiaomi2024.work.collector.service.UtilizationCollectService;
import com.txh.xiaomi2024.work.collector.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author txh
 * Quartz Job,实现定时任务
 */
@Slf4j
@Service
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CollectUtilizationJob implements Job {
    private UtilizationCollectService utilizationCollectService;

    // 这个是server提供的上报接口，这里传输数据给server
    @DubboReference
    private UtilizationUploadService utilizationUploadService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        utilizationCollectService = SpringUtil.getBean(UtilizationCollectService.class);
        utilizationUploadService = SpringUtil.getBean(UtilizationUploadService.class);
        String hostname = getHostName();
        log.info("当前主机：{}", hostname);
        double cpuUtilization = utilizationCollectService.getCpuUtilization();
        double memoryUtilization = utilizationCollectService.getMemoryUtilization();
        long timestamp = System.currentTimeMillis();
        // 调用service执行存储，将cpuUtilization与memoryUtilization作为参数传递给service
        utilizationUploadService.uploadUtilization(
                "cpu.used.percent",
                hostname,
                60,
                timestamp,
                cpuUtilization);
        utilizationUploadService.uploadUtilization(
                "mem.used.percent",
                hostname,
                60,
                timestamp,
                memoryUtilization);
    }

    // 获取当前主机名
    private String getHostName(){
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("hostname");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String hostname = br.readLine();
            p.destroy();
            return hostname;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
