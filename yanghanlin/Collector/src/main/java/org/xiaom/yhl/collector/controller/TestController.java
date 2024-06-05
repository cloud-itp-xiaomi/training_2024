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
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/memory-usage")
    public String getMemoryUsage() {
        try {
            double memoryUsage = cpuAndMemUsageService.getMemoryUsage();
            return "Memory Usage: " + memoryUsage;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}