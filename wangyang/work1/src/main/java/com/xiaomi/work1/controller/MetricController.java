package com.xiaomi.work1.controller;

import com.xiaomi.work1.bean.Metric;
import com.xiaomi.work1.bean.Result;
import com.xiaomi.work1.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: MetricController
 * Package: com.xiaomi.work1.controller
 * Description:前端控制器
 *
 * @Author WangYang
 * @Create 2024/5/24 13:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/metric")
public class MetricController {

    @Autowired
    private MetricService metricService;

    /**
     * 上报信息接口
     * @param metrics 包含上传信息的集合
     * @return
     */
    @PostMapping("/upload")
    public Result<?> uploadMetrics(@RequestBody List<Metric> metrics){
        return  metricService.uploadMetrics(metrics);
    }

    /**
     * 查询信息接口
     * @param endpoint 要查询的机器
     * @param metric 查询的指标
     * @param startTs 查询开始时间
     * @param endTs 查询结束时间
     * @return
     */
    @GetMapping("/query")
    public Result<?> queryMetrics(@RequestParam String endpoint,
                               @RequestParam String metric,
                               @RequestParam Long startTs,
                               @RequestParam Long endTs){
        return metricService.queryMetrics(endpoint, metric,startTs,endTs);
    }

    /**
     * 测试
     * @return
     */
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
