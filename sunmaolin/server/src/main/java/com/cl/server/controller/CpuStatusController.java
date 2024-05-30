package com.cl.server.controller;

import com.cl.server.entity.CpuStatus;
import com.cl.server.entity.CpuStatusResp;
import com.cl.server.entity.Result;
import com.cl.server.service.CpuStatusService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CpuStatus)表控制层
 *
 * @author tressures
 * @date  2024-05-26 17:05:56
 */
@Slf4j
@RestController
@RequestMapping("/api/metric")
public class CpuStatusController {

    @Resource
    private CpuStatusService cpuStatusService;

    @PostMapping("/upload")
    public Result uploadMetric(@RequestBody List<CpuStatus> cpuStatusList){
        try {
            cpuStatusService.uploadMetrics(cpuStatusList);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @GetMapping("/query")
    public Result<List<CpuStatus>> queryMetrics(@RequestParam(value = "endpoint",required = false) String endpoint,
                                               @RequestParam(value = "metric",required = false) String metric,
                                               @RequestParam(value = "start_ts",required = false) Long start_ts,
                                               @RequestParam(value = "end_ts",required = false) Long end_ts){
        try{
            Preconditions.checkNotNull(endpoint,"机器名称不能为空");
            Preconditions.checkNotNull(start_ts,"起始时间不能为空");
            Preconditions.checkNotNull(end_ts,"结束时间不能为空");
            List<CpuStatusResp> list = cpuStatusService.queryMetrics(endpoint,metric,start_ts,end_ts);
            return Result.ok(list);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
}

