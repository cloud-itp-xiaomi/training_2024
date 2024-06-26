package com.collector.controller;

import com.collector.bean.request.LogQueryRequest;
import com.collector.bean.request.LogUploadRequest;
import com.collector.bean.response.LogResponse;
import com.collector.utils.MyResult;
import com.collector.service.ILogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Resource
    private ILogService iLogService;

    @PostMapping("/upload")
    public MyResult<Boolean> upload(@RequestBody @Valid List<LogUploadRequest> requests) {
        try{
            return MyResult.success(null);
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("日志保存失败!");
        }
    }

    @GetMapping("/query")
    public MyResult<LogResponse> query(@RequestBody @Valid LogQueryRequest request) {
        try{
            return MyResult.success(iLogService.queryLogInfo(request));
        }catch (Exception e){
            e.printStackTrace();
            return MyResult.failed("查询日志失败!");
        }
    }
}
