package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.example.service.UtilizationCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务类，定时采集Linux的Cpu和内存利用率
 *
 * @author liuhaifeng
 * @date 2024/06/02/16:20
 */
@Slf4j
@Component
public class CollectorTask {

    @Autowired
    private UtilizationCollectorService utilizationCollectorService;

    /**
     * 每60秒执行一次
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void collectCpuMemInfo() {
        log.info("开始定时采集Cpu和内存利用率,{}", LocalDateTime.now());
        utilizationCollectorService.upload();
    }
}
