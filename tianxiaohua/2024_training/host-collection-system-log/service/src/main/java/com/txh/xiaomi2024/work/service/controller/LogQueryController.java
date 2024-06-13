package com.txh.xiaomi2024.work.service.controller;

import com.txh.xiaomi2024.work.service.bean.Log;
import com.txh.xiaomi2024.work.service.common.CommonResult;
import com.txh.xiaomi2024.work.service.domian.po.LogDocument;
import com.txh.xiaomi2024.work.service.service.LogQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author txh
 * 查询接口
 */
@RestController
@RequestMapping(value = "/api/log")
public class LogQueryController {
    private static final Logger log = LoggerFactory.getLogger(LogQueryController.class);
    private final LogQueryService logQueryService;

    @Autowired
    public LogQueryController(LogQueryService logQueryService) {
        this.logQueryService = logQueryService;
    }

    @GetMapping("/query")
    public CommonResult<?> getLogs(@RequestParam(value = "hostname") String hostname,
                                   @RequestParam(value = "file") String file){
        Log logRs = logQueryService.getLogFromMysql(
                hostname,
                file);
        Map<String,Object> map = new HashMap<>();
        map.put("hostname",hostname);
        map.put("file",file);
        // 按换行符拆分字符串
        String[] logEntries = logRs.getLogs().split("\\n");
        String firstLine = logEntries[0].split("\\[")[1];
        List<String> lines = new ArrayList<>();
        lines.add(firstLine);
        for (int i = 1; i < logEntries.length - 1; i++) {
            lines.add(logEntries[i]);
        }
        map.put("logs",lines);
        return CommonResult.success(
                map,
                "ok");
    }

    @GetMapping("/query_es")
    public CommonResult<?> getLogsFromES(@RequestParam(value = "hostname") String hostname,
                                   @RequestParam(value = "file") String file){
        LogDocument logDocument = logQueryService.getLogFromES(
                hostname,
                file);
        Map<String,Object> map = new HashMap<>();
        String logs = logDocument.getLogs().get(0);
        String[] logEntries = logs.split("\\n");
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < logEntries.length; i++) {
            lines.add(logEntries[i]);
        }
        map.put("hostname",hostname);
        map.put("file",file);
        map.put("logs",lines);
        return CommonResult.success(
                map,
                "ok");
    }
}
