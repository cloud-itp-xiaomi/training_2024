package com.txh.xiaomi2024.work.service.controller;

import com.txh.xiaomi2024.work.service.bean.Log;
import com.txh.xiaomi2024.work.service.common.CommonResult;
import com.txh.xiaomi2024.work.service.domian.po.LogDocument;
import com.txh.xiaomi2024.work.service.service.LogQueryService;
import com.txh.xiaomi2024.work.service.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author txh
 * 查询接口
 */
@RestController
@RequestMapping(value = "/api/log")
public class LogQueryController {
    private final LogQueryService logQueryService;

    @Autowired
    public LogQueryController(LogQueryService logQueryService) {
        this.logQueryService = logQueryService;
    }

    private CommonResult<?> getCommonResult(int currentPage,
                                            int pageSize,
                                            Map<String, Object> map,
                                            List<String> lines) {
        long pageNum = (long) Math.ceil(((double) lines.size() / pageSize));
        long totalNum = lines.size();
        map.put("page_num",pageNum);
        map.put("total_num",totalNum);

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(lines.size(), fromIndex + pageSize);
        map.put("logs", lines.subList(fromIndex, toIndex));

        return CommonResult.success(map, "ok");
    }

    @GetMapping("/query")
    public CommonResult<?> getLogs(@RequestParam(value = "hostname") String hostname,
                                   @RequestParam(value = "file") String file,
                                   @RequestParam(value = "current_page") int currentPage,
                                   @RequestParam(value = "page_size") int pageSize){
        // 参数校验
        if (StringUtils.isEmpty(hostname)
                || StringUtils.isEmpty(file)
                || !PathUtil.isValidFilePath(file)
                || currentPage < 0 || pageSize < 0) {
            return CommonResult.validateFailed("参数不完整或有误");
        }

        Log logRs = logQueryService.getLogFromMysql(
                hostname,
                file);
        Map<String,Object> map = new HashMap<>();
        map.put("hostname",hostname);
        map.put("file",file);

        if (logRs == null){
            map.put("logs",null);
            return CommonResult.success(map, "ok");
        }

        if (logRs.getLogs().isEmpty()) {
            map.put("logs",null);
            return CommonResult.success(map, "ok");
        }

        String[] logEntries = logRs.getLogs().split("\\n");
        String firstLine = logEntries[0].split("\\[")[1];
        List<String> lines = new ArrayList<>();
        lines.add(firstLine);
        lines.addAll(Arrays.asList(logEntries).subList(1, logEntries.length - 1));

        return getCommonResult(currentPage, pageSize, map, lines);
    }

    @GetMapping("/query_es")
    public CommonResult<?> getLogsFromES(@RequestParam(value = "hostname") String hostname,
                                         @RequestParam(value = "file") String file,
                                         @RequestParam(value = "current_page") int currentPage,
                                         @RequestParam(value = "page_size") int pageSize){
        // 参数校验
        if (StringUtils.isEmpty(hostname)
                || StringUtils.isEmpty(file)
                || !PathUtil.isValidFilePath(file)
                || currentPage < 0 || pageSize < 0) {
            return CommonResult.validateFailed("参数不完整或有误");
        }

        LogDocument logDocument = logQueryService.getLogFromES(
                hostname,
                file);
        Map<String,Object> map = new HashMap<>();
        map.put("hostname",hostname);
        map.put("file",file);

        if (logDocument == null){
            map.put("logs",null);
            return CommonResult.success(map, "ok");
        }

        String logs = logDocument.getLogs().get(0);
        if (logs.isEmpty()) {
            map.put("logs",null);
            return CommonResult.success(map, "ok");
        }

        String[] logEntries = logs.split("\\n");
        List<String> lines = new ArrayList<>();
        Collections.addAll(lines, logEntries);

        return getCommonResult(currentPage, pageSize, map, lines);
    }
}
