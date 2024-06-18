package com.collector.controller;

import com.collector.bean.request.CollectorQueryRequest;
import com.collector.bean.request.CollectorUploadRequest;
import com.collector.bean.response.CollectorResponse;
import com.collector.utils.MyResult;
import com.collector.service.ICollectorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/metric")
public class CollectorController {
    @Resource
    private ICollectorService iCollectorService;

    @PostMapping("/upload")
    public MyResult<Boolean> upload(@RequestBody @Valid List<CollectorUploadRequest> requests) {
        try{
            return MyResult.success(iCollectorService.upload(requests));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("数据采集失败!");
        }
    }

    @GetMapping("/query")
    public MyResult<List<CollectorResponse>> query(@RequestBody @Valid CollectorQueryRequest request) {
        try{
            return MyResult.success(iCollectorService.queryCollectorInfo(request));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("查询数据失败!");
        }
    }
}
