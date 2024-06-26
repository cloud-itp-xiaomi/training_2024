package com.jiuth.sysmonitorserver.controller;

import com.jiuth.sysmonitorserver.dao.LogInfoRepository;
import com.jiuth.sysmonitorserver.dao.enity.LogInfo;
import com.jiuth.sysmonitorserver.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    private LogInfoRepository logInfoRepository;

    @PostMapping("/upload")
    public ApiResponse<Void> uploadLogs(@RequestBody List<LogInfo> logs) {
        for (LogInfo log : logs) {
            // 检查数据库中是否已存在相同 hostname 和 file 的记录
            List<LogInfo> existingLogs = logInfoRepository.findByHostnameAndFile(log.getHostname(), log.getFile());
            if (existingLogs.isEmpty()) {
                // 如果不存在，则新增记录
                logInfoRepository.save(log);
            } else {
                // 如果已存在，则更新现有记录
                LogInfo existingLog = existingLogs.getFirst();
                List<String> newLogs = log.getLogs();
                // 添加新日志信息
                existingLog.getLogs().addAll(newLogs);
                // 更新现有记录
                logInfoRepository.save(existingLog);
            }
        }
        return ApiResponse.success(null);
    }

    @GetMapping("/query")
    public ApiResponse<List<LogInfo>> queryLogs(@RequestParam String hostname, @RequestParam String file) {
        List<LogInfo> result = logInfoRepository.findByHostnameAndFile(hostname, file);
        return ApiResponse.success(result);
    }
}