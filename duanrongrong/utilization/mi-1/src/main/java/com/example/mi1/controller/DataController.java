package com.example.mi1.controller;

import com.example.mi1.domain.UploadParam;
import com.example.mi1.domain.enums.UtilizationEnums;
import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;
import com.example.mi1.domain.vo.QueryVO;
import com.example.mi1.service.CPUUtilizationService;
import com.example.mi1.common.api.CommonResult;
import com.example.mi1.service.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class DataController {
    private final CPUUtilizationService cpuUtilizationService;
    private final Storage storage;

    @Autowired
    DataController(CPUUtilizationService cpuUtilizationService, Storage storage) {
        this.cpuUtilizationService = cpuUtilizationService;
        this.storage = storage;
    }

    @PostMapping(value = "/metric/upload")
    public CommonResult<?> postData(@RequestBody UploadParam uploadParam) {
        cpuUtilizationService.checkEndpoint(uploadParam.getEndpoint());
        if (uploadParam.getMetric().equals(UtilizationEnums.CPU_USED_PERCENT)) {
            cpuUtilizationService.setCPUUtilization(uploadParam);
        } else if (uploadParam.getMetric().equals(UtilizationEnums.MEM_USED_PERCENT)) {
            cpuUtilizationService.setMemUtilization(uploadParam);
        } else {
            return CommonResult.failed("metric error");
        }
        log.info("数据上传成功！endpoint: {}, metric: {}", uploadParam.getEndpoint(), uploadParam.getMetric());
        return CommonResult.success(null, "ok");
    }

    @GetMapping(value = "/metric/query")
    public CommonResult<List<QueryVO>> getData(@RequestParam(value = "endpoint") String endpoint,
                                               @RequestParam(value = "metric") String metric,
                                               @RequestParam(value = "start_ts") Long start,
                                               @RequestParam(value = "end_ts") Long end,
                                               @RequestParam(value = "current_page") Integer currentPage) {
        List<QueryVO> utilizationList = null;
        try {
            utilizationList = cpuUtilizationService.getUtilization(endpoint, metric, start, end, currentPage);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(utilizationList, "ok");
    }

    @GetMapping(value = "/endpoint/query")
    public CommonResult<List<String>> getEndpointData() {
        List<String> res = cpuUtilizationService.getEndpoints();
        return CommonResult.success(res, "ok");
    }

    @GetMapping(value = "/file/query")
    public CommonResult<List<String>> getFileData() {
        List<String> res = cpuUtilizationService.getFile();
        return CommonResult.success(res, "ok");
    }

    @GetMapping(value = "/log/query")
    public CommonResult<LogQueryVO> getLogData(@RequestParam(value = "hostname") String hostname,
                                               @RequestParam(value = "file") String fileName,
                                               @RequestParam(value = "current_page") Integer currentPage) {
        LogQueryVO res = storage.load(hostname, fileName, currentPage, 10);
        return CommonResult.success(res, "ok");
    }

    @PostMapping(value = "/log/upload")
    public CommonResult<?> postData(@RequestBody Log log) {
        cpuUtilizationService.checkFile(log.getFile());
        storage.save(log);
        return CommonResult.success(null, "ok");
    }
}
