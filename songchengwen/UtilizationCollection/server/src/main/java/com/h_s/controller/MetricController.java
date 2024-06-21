package com.h_s.controller;

import com.h_s.dto.ApiResponse;
import com.h_s.entity.Metric;
import com.h_s.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metric")
public class MetricController {
    @Autowired
    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMetrics(@RequestBody List<Metric> metrics) {
        metricService.saveMetrics(metrics);
        return ResponseEntity.ok("successful");
    }

    @GetMapping("/query")
    public ResponseEntity<ApiResponse> queryMetrics(@RequestParam String endpoint,
                                                     @RequestParam(required = false) String metric,
                                                     @RequestParam long start_ts,
                                                     @RequestParam long end_ts) {
        try {
            List<Metric> metrics = metricService.getMetrics(endpoint, metric, start_ts, end_ts);
            if (metrics.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error(404, "No data"));
            }

            Map<String, List<ApiResponse.Value>> groupedMetrics = metrics.stream()
                    .collect(Collectors.groupingBy(
                            Metric::getMetric,
                            Collectors.mapping(
                                    m -> new ApiResponse.Value(m.getTimestamp(), m.getValue()),
                                    Collectors.toList()
                            )
                    ));

            List<ApiResponse.Data> data = groupedMetrics.entrySet().stream()
                    .map(entry -> new ApiResponse.Data(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(data));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "Internal server error"));
        }
    }
}
