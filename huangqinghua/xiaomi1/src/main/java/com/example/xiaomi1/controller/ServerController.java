package com.example.xiaomi1.controller;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.result.Result;
import com.example.xiaomi1.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    private MetricService metricService;

    @PostMapping("/metric/upload")
    public Result receiveMetric(@RequestBody Metric metric) {
        //metric.setTimeStamp(LocalDateTime.now());
        System.out.println(metric.getTimeStamp());
        metricService.saveMetric(metric);
        return new Result().success();
    }
}
