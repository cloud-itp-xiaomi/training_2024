package com.h_s.controller;

import com.h_s.service.LogService;
import com.h_s.service.LogServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    private final LogServiceFactory logServiceFactory;

    @Autowired
    public LogController(LogServiceFactory logServiceFactory) {
        this.logServiceFactory = logServiceFactory;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody Map<String, Object> request) {
        String hostname = (String) request.get("hostname");
        String file = (String) request.get("file");
        List<String> logs = (List<String>) request.get("logs");
        String logStorage = (String) request.get("logStorage");

        LogService logService = logServiceFactory.getLogService(logStorage);
        logService.saveLogs(hostname, file, logs);
        return ResponseEntity.ok("日志内容上传成功");

    }

    @GetMapping("/query")
    public ResponseEntity<?> queryLogs(@RequestParam String hostname,
                                       @RequestParam String file) {
        LogService logService = logServiceFactory.getLogService("mysql");
        List<Map<String, String>> logs = logService.queryLogs(hostname, file);
        return ResponseEntity.ok(logs);
    }
}
