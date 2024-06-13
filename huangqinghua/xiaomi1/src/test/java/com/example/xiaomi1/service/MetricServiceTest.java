package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.mapper.MetricMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class MetricServiceTest {

    @Autowired
    private MetricService metricService;

    @MockBean
    private MetricMapper metricMapper;

    // @MockBean
    // private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void saveMetric() {
        Metric metric = new Metric();
        metric.setMetric("testMetric");
        metric.setEndpoint("testComputer");
        metric.setStep(60);
        metric.setTimestamp(1000000000);
        metric.setValue(10.22);

        metricService.saveMetric(metric);

        // 验证MySQL
        verify(metricMapper, times(1)).insert(metric);

        // 手动断言传入的参数值与预期值相符
        ArgumentCaptor<Metric> captor = ArgumentCaptor.forClass(Metric.class);
        verify(metricMapper).insert(captor.capture());
        Metric capturedMetric = captor.getValue();

        assertEquals("testMetric", capturedMetric.getMetric());
        assertEquals("testComputer", capturedMetric.getEndpoint());
        assertEquals(60, capturedMetric.getStep());
        assertEquals(1000000000, capturedMetric.getTimestamp());
        assertEquals(10.22, capturedMetric.getValue());
    }

    @Test
    public void getMetrics() {
        Metric metric = new Metric();
    }
}