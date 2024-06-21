package com.xiaomi.server.controller;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.Entity.vo.LogEntryRequest;
import com.xiaomi.server.Entity.vo.UploadLogsRequest;
import com.xiaomi.server.common.Result;
import com.xiaomi.server.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/log")
public class LogEntryController {
    @Autowired
    private LogEntryService logEntryService;

    @PostMapping("/upload")
    public Result uploadLogs(@RequestBody UploadLogsRequest request) {
        try {
            String logStorage = request.getLogStorage();
            List<LogEntryRequest> logEntryRequests = request.getLogs();

            List<LogEntry> logEntries = new ArrayList<>();
            for (LogEntryRequest logEntryRequest : logEntryRequests) {
                for (String log : logEntryRequest.getLogs()) {
                    LogEntry logEntry = new LogEntry();
                    logEntry.setHostname(logEntryRequest.getHostname());
                    logEntry.setFile(logEntryRequest.getFile());
                    logEntry.setLogs(Collections.singletonList(log));
                    logEntry.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    logEntries.add(logEntry);
                }
            }

            logEntryService.saveLogEntries(logEntries);


            return Result.success("succeed to upload logs！");
        } catch (Exception e) {
            return Result.error("Failed to upload logs: " + e.getMessage());
        }
    }




}
