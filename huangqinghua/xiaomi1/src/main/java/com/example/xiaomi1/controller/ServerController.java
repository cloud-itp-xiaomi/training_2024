package com.example.xiaomi1.controller;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.entity.MetricData;
import com.example.xiaomi1.entity.MetricResponse;
import com.example.xiaomi1.result.Result;
import com.example.xiaomi1.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ServerController {

    @Autowired
    private MetricService metricService;

    // 上报接口
    @PostMapping("/metric/upload")
    public Result receiveMetric(@RequestBody Metric metric) {
        try {
            metricService.saveMetric(metric);
            return new Result().success("");  // 返回成功的结果，并带上接收到的数据
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().failure(500, "Error saving metric: " + e.getMessage());
        }
    }


    // 查询接口
    @GetMapping("/metric/query")
    public Result queryMetrics(
            @RequestParam String endpoint,
            @RequestParam(required = false) String metric,
            @RequestParam long start_ts,
            @RequestParam long end_ts) {
        try {
            // 获取所有符合条件的数据
            List<MetricData> metricDataList = metricService.getMetrics(endpoint, metric, start_ts, end_ts);

            // 按照 metric 分组
            Map<String, List<MetricData>> groupedByMetric = metricDataList.stream()
                    .collect(Collectors.groupingBy(MetricData::getMetric));

            // 转换为 MetricResponse 格式
            List<MetricResponse> responseList = new ArrayList<>();
            for (Map.Entry<String, List<MetricData>> entry : groupedByMetric.entrySet()) {
                MetricResponse metricResponse = new MetricResponse();
                metricResponse.setMetric(entry.getKey());

                List<MetricResponse.ValueData> valueDataList = entry.getValue().stream()
                        .map(data -> {
                            MetricResponse.ValueData valueData = new MetricResponse.ValueData();
                            valueData.setTimestamp(data.getTimestamp());
                            valueData.setValue(data.getValue());
                            return valueData;
                        })
                        .collect(Collectors.toList());

                metricResponse.setValues(valueDataList);
                responseList.add(metricResponse);
            }

            return new Result().success(responseList);  // 返回成功的结果，并带上转换后的数据
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ok");
            return new Result().failure(500, "Error get metric: " + e.getMessage());
        }
    }

}
