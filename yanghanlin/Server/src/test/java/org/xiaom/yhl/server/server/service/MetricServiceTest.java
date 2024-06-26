package org.xiaom.yhl.server.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.xiaom.yhl.server.server.entity.Metric;
import org.xiaom.yhl.server.server.repository.MetricRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * ClassName: MetricServiceTest
 * Package: org.xiaom.yhl.server.server.service
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/6/10 22:33
 * @Version 1.0
 */
public class MetricServiceTest {

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @InjectMocks
    private MetricService metricService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    @Test
    public void testSaveMetric() {
        Metric metric = new Metric();
        metric.setMetric("cpu.used.percent");
        metric.setEndpoint("my-computer");
        metric.setTimestamp(System.currentTimeMillis() / 1000);
        metric.setStep(60);
        metric.setValue(50.0);

        when(listOperations.size("recentCpuMetrics")).thenReturn(11L);

        metricService.saveMetric(metric);

        ArgumentCaptor<Metric> metricCaptor = ArgumentCaptor.forClass(Metric.class);
        verify(metricRepository, times(1)).save(metricCaptor.capture());
        assertEquals("cpu.used.percent", metricCaptor.getValue().getMetric());

        String redisKey = "recentCpuMetrics";
        verify(listOperations, times(1)).leftPush(redisKey, metric);
        verify(listOperations, times(1)).rightPop(redisKey);
    }

    @Test
    public void testQueryMetrics() {
        String endpoint = "my-computer";
        String metric = "cpu.used.percent";
        long startTs = 1620000000L;
        long endTs = 1620003600L;

        List<Metric> expectedMetrics = List.of(new Metric());
        when(metricRepository.findByEndpointAndMetricAndTimestampBetween(endpoint, metric, startTs, endTs))
                .thenReturn(expectedMetrics);

        List<Metric> actualMetrics = metricService.queryMetrics(endpoint, metric, startTs, endTs);
        assertEquals(expectedMetrics, actualMetrics);
    }
}