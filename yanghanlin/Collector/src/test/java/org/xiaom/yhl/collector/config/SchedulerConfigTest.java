package org.xiaom.yhl.collector.config;

/**
 * ClassName: SchedulerConfigTest
 * Package: org.xiaom.yhl.collector.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/6/9 21:33
 * @Version 1.0
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Scheduled;
import org.xiaom.yhl.collector.service.CPUAndMemUsageService;

import static org.mockito.Mockito.*;

class SchedulerConfigTest {

    @Mock
    private CPUAndMemUsageService cpuAndMemUsageService;

    @Mock
    private MetricsUploader metricsUploader;

    @InjectMocks
    private SchedulerConfig schedulerConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCollectUsage() throws Exception {
        when(cpuAndMemUsageService.getCpuUsage()).thenReturn(0.6);
        when(cpuAndMemUsageService.getMemoryUsage()).thenReturn(0.7);

        schedulerConfig.collectUsage();

        verify(cpuAndMemUsageService, times(1)).getCpuUsage();
        verify(cpuAndMemUsageService, times(1)).getMemoryUsage();
        verify(metricsUploader, times(1)).uploadMetric("cpu.used.percent", 0.6);
        verify(metricsUploader, times(1)).uploadMetric("mem.used.percent", 0.7);
    }
}