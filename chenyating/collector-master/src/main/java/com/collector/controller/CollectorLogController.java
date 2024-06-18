package com.collector.controller;

import com.collector.bean.request.CollectorLogQueryRequest;
import com.collector.bean.request.CollectorLogUploadRequest;
import com.collector.bean.response.CollectorLogResponse;
import com.collector.utils.MyResult;
import com.collector.service.ICollectorLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class CollectorLogController {

    @Resource
    private ICollectorLogService iCollectorLogService;

    @PostMapping("/upload")
    public MyResult<Boolean> upload(@RequestBody @Valid List<CollectorLogUploadRequest> requests) {
        try{
            return MyResult.success(iCollectorLogService.upload(requests));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("日志保存失败!");
        }
    }

    @GetMapping("/query")
    public MyResult<CollectorLogResponse> query(@RequestBody @Valid CollectorLogQueryRequest request) {
        try{
            return MyResult.success(iCollectorLogService.queryLogInfo(request));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("查询日志失败!");
        }
    }
}
