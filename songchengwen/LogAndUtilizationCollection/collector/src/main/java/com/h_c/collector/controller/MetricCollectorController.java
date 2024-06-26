package com.h_c.collector.controller;

import com.h_c.collector.service.MetricCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collector")
public class MetricCollectorController {
    @Autowired
    private final MetricCollectorService metricCollectorService;

    public MetricCollectorController(MetricCollectorService metricCollectorService) {
        this.metricCollectorService = metricCollectorService;
    }

    @GetMapping("/collect")
    public ResponseEntity<String> collectMetrics() {
        metricCollectorService.collectAndSendMetrics();
        return ResponseEntity.ok("Metrics collected and sent to server");
    }
}
