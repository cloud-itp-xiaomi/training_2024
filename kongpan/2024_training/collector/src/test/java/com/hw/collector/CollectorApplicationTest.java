package com.hw.collector;

import com.hw.collector.config.nacos.NacosJsonConfig;
import com.hw.collector.task.MetricCollector;
import com.hw.collector.watcher.CPUWatcher;
import com.hw.collector.watcher.MemoryWatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author mrk
 * @create 2024-06-07-22:14
 */
@SpringBootTest(classes = CollectorApplication.class)
class CollectorApplicationTest {

    @Autowired
    private NacosJsonConfig nacosJsonConfig;

    /**
     * 测试 cpu 和 内存 利用率采集功能
     * @throws IOException
     */
    @Test
    public void testMetricCollect() throws IOException {
        CPUWatcher cpuWatcher = new CPUWatcher();
        MemoryWatcher memoryWatcher = new MemoryWatcher();

        double cpuUsage = MetricCollector.formatUsage(cpuWatcher.getCpuUsage());
        double memoryUsage = MetricCollector.formatUsage(memoryWatcher.getMemoryUsage());

        System.out.println("cpu usage: " + cpuUsage + " memory usage: " + memoryUsage);
    }

    @Test
    public void testNacosConfig() {
        System.out.println(nacosJsonConfig.getConfigDTO().getLogStorage());
    }

}