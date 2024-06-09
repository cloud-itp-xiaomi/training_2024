package org.xiaom.yhl.collector.config;

/**
 * ClassName: MetricsUploaderTest
 * Package: org.xiaom.yhl.collector.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/6/9 21:02
 * @Version 1.0
 */
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

public class MetricsUploaderTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MetricsUploader metricsUploader;

    public MetricsUploaderTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadMetric() {
        String metric = "cpu.used.percent";
        double value = 60.1;
        String url = "http://localhost:8080/api/metric/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> metricData = new HashMap<>();
        metricData.put("metric", metric);
        metricData.put("endpoint", "my-computer");
        metricData.put("timestamp", System.currentTimeMillis() / 1000);
        metricData.put("step", 60);
        metricData.put("value", value);

        Map<String, Object>[] metricArray = new Map[]{metricData};

        HttpEntity<Map<String, Object>[]> request = new HttpEntity<>(metricArray, headers);

        metricsUploader.uploadMetric(metric, value);

        verify(restTemplate).postForEntity(url, request, String.class);
    }
}

