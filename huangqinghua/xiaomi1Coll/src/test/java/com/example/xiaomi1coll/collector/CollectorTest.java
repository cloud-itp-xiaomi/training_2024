package com.example.xiaomi1coll.collector;

import com.example.xiaomi1coll.entity.Metric;
import com.sun.management.OperatingSystemMXBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CollectorTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OperatingSystemMXBean osBean;

    @InjectMocks
    private Collector collector;

    @Captor
    private ArgumentCaptor<HttpEntity<Metric>> requestEntityCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟 OperatingSystemMXBean 的行为
        when(osBean.getCpuLoad()).thenReturn(0.5);
        when(osBean.getTotalMemorySize()).thenReturn(10000L);
        when(osBean.getFreeMemorySize()).thenReturn(4000L);
    }

    @Test
    void testCollectAndReport() throws InterruptedException {
        // 模拟 RestTemplate 的响应
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(org.springframework.http.HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn("Success");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // 创建一个自定义的线程池用于测试
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        collector = new Collector(restTemplate, osBean, executorService);

        // 运行 collectAndReport 方法
        collector.collectAndReport();

        // 关闭线程池并等待所有任务完成
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // 验证是否发送了指标数据
        verify(restTemplate, times(2)).exchange(anyString(), any(HttpMethod.class), requestEntityCaptor.capture(), eq(String.class));

        // 验证捕获的指标数据
        for (HttpEntity<Metric> requestEntity : requestEntityCaptor.getAllValues()) {
            Metric metric = requestEntity.getBody();
            assertNotNull(metric);
            assertEquals("my-computer", metric.getEndpoint());
            assertEquals(60, metric.getStep());
            assertTrue(metric.getTimestamp() <= Instant.now().getEpochSecond());

            if (metric.getMetric().equals("cpu.used.percent")) {
                assertEquals(50.0, metric.getValue());
            } else if (metric.getMetric().equals("mem.used.percent")) {
                assertEquals(60.0, metric.getValue());
            } else {
                fail("Unexpected metric: " + metric.getMetric());
            }
        }
    }

    @Test
    void testGetMemoryUsage() {
        double memoryUsage = collector.getMemoryUsage();
        assertEquals(60.0, memoryUsage, 0.01);
    }

    @Test
    void testGetCpuUsage() {
        double cpuUsage = collector.getCpuUsage();
        assertEquals(50.0, cpuUsage, 0.01);
    }

    @Test
    void testSendMetricTimeout() {
        // 模拟请求超时异常
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new org.springframework.web.client.ResourceAccessException("Request timeout"));

        Metric metric = new Metric();
        metric.setMetric("cpu.used.percent");
        metric.setEndpoint("my-computer");
        metric.setTimestamp(Instant.now().getEpochSecond());
        metric.setStep(60);
        metric.setValue(50.0);

        collector.sendMetric(metric);

        // 验证是否记录了错误日志
        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class));
    }
}
