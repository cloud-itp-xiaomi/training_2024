package org.xiaom.yhl.collector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xiaom.yhl.collector.service.CPUAndMemUsageService;
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

    private final CPUAndMemUsageService cpuAndMemUsageService;
    private final MetricsUploader metricsUploader;

    @Autowired
    public SchedulerConfig(CPUAndMemUsageService cpuAndMemUsageService, MetricsUploader metricsUploader) {
        this.cpuAndMemUsageService = cpuAndMemUsageService;
        this.metricsUploader = metricsUploader;
    }

    @Scheduled(fixedRate = 60000)
    public void collectUsage() {
        try {
            double cpuUsage = cpuAndMemUsageService.getCpuUsage();
            double memoryUsage = cpuAndMemUsageService.getMemoryUsage();
            System.out.println(String.format("CPU Usage: %.2f%%", cpuUsage * 100));
            System.out.println(String.format("Memory Usage: %.2f%%", memoryUsage * 100));
            metricsUploader.uploadMetric("cpu.used.percent", cpuUsage);
            metricsUploader.uploadMetric("mem.used.percent", memoryUsage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
