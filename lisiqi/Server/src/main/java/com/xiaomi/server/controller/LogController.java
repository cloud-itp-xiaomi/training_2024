package com.xiaomi.server.controller;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.common.Result;
import com.xiaomi.server.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/upload")
    public Result uploadLogs(@RequestBody List<LogEntry> logEntries) {
        try {
            logService.storeLogs(logEntries);
            return Result.success("succeed to upload metrics！");
        } catch (Exception e) {
            return Result.error("Failed to upload metrics: " + e.getMessage());
        }
    }

    @GetMapping("/query")
    public Result queryLogs(@RequestParam String hostname, @RequestParam String file) {
        try {
            List<String> logs = logService.queryLogs(hostname, file);
            Map<String, Object> data = new HashMap<>();
            data.put("hostname", hostname);
            data.put("file", file);
            data.put("logs", logs);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("Failed to query metrics: " + e.getMessage());
        }
    }
}
