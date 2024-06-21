package com.example.xiaomi1.controller;

import com.example.xiaomi1.entity.*;
import com.example.xiaomi1.result.Result;
import com.example.xiaomi1.service.LogService;
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

    @Autowired
    private LogService logService;

    // 指标上报接口
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

    // 指标查询接口
    @GetMapping("/metric/query")
    public Result queryMetrics(
            @RequestParam String endpoint,
            @RequestParam(required = false) String metric,
            @RequestParam long start_ts,
            @RequestParam long end_ts) {

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
    }

    // 日志上报接口，本地文件存储版
    @PostMapping("/log/upload/local")
    public Result receiveLogLocal(@RequestBody Logs logs) {
        System.out.println(logs);
        try {
            for (String log : logs.getLogs()) {
                Log log1 = createLog(logs.getHostname(), logs.getFile(), log);
                logService.saveLogLocal(log1);
            }
            return new Result().success("");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().failure(500, "Error saving log in local: " + e.getMessage());
        }
    }

    // 日志上报接口，MySQL存储版
    @PostMapping("/log/upload/MySQL")
    public Result receiveLogMysql(@RequestBody Logs logs) {
        System.out.println(logs);
        try {
            for (String log : logs.getLogs()) {
                Log log1 = createLog(logs.getHostname(), logs.getFile(), log);
                logService.saveLogMysql(log1);
            }
            return new Result().success("");  // 返回成功的结果，并带上接收到的数据
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().failure(500, "Error saving log in MySQL: " + e.getMessage());
        }
    }

    // 日志查询接口
    @GetMapping("/log/query")
    public Result queryLogs(
            @RequestParam String hostname,
            @RequestParam String file){

        List<Log> logs = logService.getLog(hostname, file);
        return getResult(hostname, file, logs);
    }

    @GetMapping("/log/query/by-MySQL")
    public Result queryLogsByMysql(
            @RequestParam String hostname,
            @RequestParam String file){

        List<Log> logs = logService.getLogByMysql(hostname, file);
        return getResult(hostname, file, logs);
    }

    @GetMapping("/log/query/by-local")
    public Result queryLogsByLocal(
            @RequestParam String hostname,
            @RequestParam String file){

        List<Log> logs = logService.getLogByLocal(hostname, file);
        return getResult(hostname, file, logs);
    }

    // 日志封装返回查询结果
    private Result getResult(@RequestParam String hostname, @RequestParam String file, List<Log> logs) {
        LogResponse logResponse = new LogResponse();
        logResponse.setHostname(hostname);
        logResponse.setFile(file);

        List<String> logMessages = logs.stream()
                .map(Log::getLog)
                .collect(Collectors.toList());
        logResponse.setLogs(logMessages);

        return new Result().success(logResponse);
    }

    public Log createLog(String hostname, String file, String logContent) {
        Log log = new Log();
        log.setHostname(hostname);
        log.setFile(file);
        log.setLog(logContent);
        return log;
    }
}
