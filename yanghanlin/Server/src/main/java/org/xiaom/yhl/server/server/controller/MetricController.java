package org.xiaom.yhl.server.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xiaom.yhl.server.server.entity.Metric;
import org.xiaom.yhl.server.server.service.MetricService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: MetricController
 * Package: org.xiaom.yhl.server.server.controller
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/30 15:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMetrics(@RequestBody List<Metric> metrics) {
        for (Metric metric : metrics) {
            metricService.saveMetric(metric);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "ok");
        response.put("data", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/query")
    public ResponseEntity<?> queryMetrics(
            @RequestParam String endpoint,
            @RequestParam(required = false) String metric,
            @RequestParam long startTs,
            @RequestParam long endTs) {
        List<Metric> metrics = metricService.queryMetrics(endpoint, metric, startTs, endTs);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "ok");
        response.put("data", metrics);
        return ResponseEntity.ok(response);
    }
}
