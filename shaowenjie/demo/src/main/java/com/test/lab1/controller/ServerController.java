package com.test.lab1.controller;

import com.test.lab1.entity.Metric;
import com.test.lab1.entity.MetricData;
import com.test.lab1.entity.MetricResponse;
import com.test.lab1.result.Result;
import com.test.lab1.service.MetricService;
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

    /*
     * 上报接口
     * */
    @PostMapping("/metric/upload")
    public Result receiveMetric(@RequestBody Metric metric) {
        try {
            metricService.saveMetric(metric);
            return new Result().success("");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().failure(500, "Error saving metric: " + e.getMessage());
        }
    }

    /*
    * 查询接口
    * */
    @GetMapping("/metric/query")
    public Result queryMetrics(
            @RequestParam String endpoint,
            @RequestParam(required = false) String metric,
            @RequestParam long start_ts,
            @RequestParam long end_ts) {

        List<MetricData> metricDataList = metricService.getMetrics(endpoint, metric, start_ts, end_ts);

        Map<String, List<MetricData>> groupedByMetric = metricDataList.stream()
                .collect(Collectors.groupingBy(MetricData::getMetric));

        List<MetricResponse> responseList = resultMetrics(groupedByMetric);

        return new Result().success(responseList);
    }

    /*
     * 按照要求封装返回
     * */
    public List<MetricResponse> resultMetrics(Map<String, List<MetricData>> groupedByMetric){
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

        return responseList;
    }

}
