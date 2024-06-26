package com.collector.controller;

import com.collector.bean.request.MetircQueryRequest;
import com.collector.bean.request.MetircUploadRequest;
import com.collector.bean.response.MetricResponse;
import com.collector.utils.MyResult;
import com.collector.service.IMetricService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/metric")
public class MetricController {
    @Resource
    private IMetricService iMetricService;

    @PostMapping("/upload")
    public MyResult<Boolean> upload(@RequestBody @Valid List<MetircUploadRequest> requests) {
        try{
            return MyResult.success(null);
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("数据采集失败!");
        }
    }

    @GetMapping("/query")
    public MyResult<List<MetricResponse>> query(@RequestBody @Valid MetircQueryRequest request) {
        try{
            return MyResult.success(iMetricService.queryMetricInfo(request));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("查询数据失败!");
        }
    }
}
