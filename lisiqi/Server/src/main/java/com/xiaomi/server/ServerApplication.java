package com.xiaomi.server;

import com.xiaomi.server.Entity.Metric;
import com.xiaomi.server.common.Result;
import com.xiaomi.server.service.MetricService;
import com.xiaomi.server.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class ServerApplication {

    @Autowired
    private MetricService metricService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/api/metric/upload")
    public Result uploadMetrics(@RequestBody Metric[] metrics) {
        try {
            for (Metric metric : metrics) {
                metricService.save(metric);
                redisService.saveMetric(metric);
            }
            return Result.success("succeed to upload metrics！");
        } catch (Exception e) {
            return Result.error("Failed to upload metrics: " + e.getMessage());
        }
    }

    @GetMapping("/api/metric/query")
    public Result queryMetric(@RequestParam String endpoint,
                              @RequestParam(required = false) String metric,
                              @RequestParam long start_ts,
                              @RequestParam long end_ts) {
        // 这里查询指标数据，从MySQL或Redis中获取数据并返回

        try {
            List<Metric> metrics = metricService.queryMetrics(endpoint, metric, start_ts, end_ts);
            List<Map<String, Object>> data = metrics.stream()
                    .collect(Collectors.groupingBy(Metric::getMetric))
                    .entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> metricMap = new HashMap<>();
                        metricMap.put("metric", entry.getKey());
                        List<Map<String, Object>> values = entry.getValue().stream()
                                .map(m -> {
                                    Map<String, Object> valueMap = new HashMap<>();
                                    valueMap.put("timestamp", m.getTimestamp());
                                    valueMap.put("value", m.getValue());
                                    return valueMap;
                                })
                                .collect(Collectors.toList());
                        metricMap.put("values", values);
                        return metricMap;
                    })
                    .collect(Collectors.toList());
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("Failed to query metrics: " + e.getMessage());
        }

    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
