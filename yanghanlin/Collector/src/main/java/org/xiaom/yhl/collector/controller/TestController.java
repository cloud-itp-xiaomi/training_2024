package org.xiaom.yhl.collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaom.yhl.collector.service.CPUAndMemUsageService;


/**
 * ClassName: TestController
 * Package: org.xiaom.yhl.collector.controller
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/29 21:13
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private CPUAndMemUsageService cpuAndMemUsageService;

    @GetMapping("/cpu-usage")
    public String getCpuUsage() {
        try {
            double cpuUsage = cpuAndMemUsageService.getCpuUsage();
            return "CPU Usage: " + cpuUsage;
        } catch (Exception exception_1) {
            exception_1.printStackTrace();
            return "Error: " + exception_1.getMessage();
        }
    }

    @GetMapping("/memory-usage")
    public String getMemoryUsage() {
        try {
            double memoryUsage = cpuAndMemUsageService.getMemoryUsage();
            return "Memory Usage: " + memoryUsage;
        } catch (Exception exception_2) {
            exception_2.printStackTrace();
            return "Error: " + exception_2.getMessage();
        }
    }
}