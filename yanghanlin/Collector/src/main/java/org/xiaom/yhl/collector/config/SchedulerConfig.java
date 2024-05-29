package org.xiaom.yhl.collector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xiaom.yhl.collector.service.CpuUsageService;

/**
 * ClassName: SchedulerConfig
 * Package: org.xiaom.yhl.collector.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/29 21:03
 * @Version 1.0
 */
@Component
public class SchedulerConfig {
    @Autowired
    private CpuUsageService cpuUsageService;

    @Scheduled(fixedRate = 60000)
    public void collectCpuUsage() {
        try {
            double cpuUsage = cpuUsageService.getCpuUsage();
            System.out.println("CPU Usage: " + cpuUsage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
