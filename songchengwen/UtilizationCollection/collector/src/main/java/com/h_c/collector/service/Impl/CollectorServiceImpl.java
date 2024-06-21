package com.h_c.collector.service.Impl;

import com.h_c.collector.service.CollectorService;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;

@Service
public class CollectorServiceImpl implements CollectorService {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    @Override
    public double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return osBean.getCpuLoad() * 100;
    }

    @Override
    public double getMemUsage() {
        return (1 - (double) osBean.getFreeMemorySize() / osBean.getTotalMemorySize()) * 100;
    }
}
