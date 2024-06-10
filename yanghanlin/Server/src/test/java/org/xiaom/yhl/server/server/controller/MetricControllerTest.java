package org.xiaom.yhl.server.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xiaom.yhl.server.server.entity.Metric;
import org.xiaom.yhl.server.server.service.MetricService;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * ClassName: MetricControllerTest
 * Package: org.xiaom.yhl.server.server.controller
 * Description:Test the function of the MetricController
 *
 * @Author 杨瀚林
 * @Create 2024/6/10 22:40
 * @Version 1.0
 */
public class MetricControllerTest {

    @Mock
    private MetricService metricService;

    @InjectMocks
    private MetricController metricController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadMetrics() {
        Metric metric = new Metric();
        List<Metric> metrics = List.of(metric);

        ResponseEntity<?> response = metricController.uploadMetrics(metrics);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", ((HashMap<String, Object>) response.getBody()).get("message"));

        verify(metricService, times(1)).saveMetric(metric);
    }

    @Test
    public void testQueryMetrics() {
        String endpoint = "my-computer";
        String metric = "cpu.used.percent";
        long startTs = 1620000000L;
        long endTs = 1620003600L;

        List<Metric> expectedMetrics = List.of(new Metric());
        when(metricService.queryMetrics(endpoint, metric, startTs, endTs)).thenReturn(expectedMetrics);

        ResponseEntity<?> response = metricController.queryMetrics(endpoint, metric, startTs, endTs);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMetrics, ((HashMap<String, Object>) response.getBody()).get("data"));

        verify(metricService, times(1)).queryMetrics(endpoint, metric, startTs, endTs);
    }
}