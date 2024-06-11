package com.hw.collector.client;

import com.hw.collector.dto.Metrics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author mrk
 * @create 2024-06-06-14:35
 */
@FeignClient(name = "server-service", contextId = "metric")
public interface MetricClient {

    @PostMapping("/api/metric/upload")
    void uploadMetrics(List<Metrics> metrics);
}
