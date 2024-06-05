package org.xiaom.yhl.collector.service;

/**
 * ClassName: CPUAndMemUsageService
 * Package: org.xiaom.yhl.collector.service
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/31 14:13
 * @Version 1.0
 */
public interface CPUAndMemUsageService {
    double getCpuUsage() throws Exception;
    double getMemoryUsage() throws Exception;
}
