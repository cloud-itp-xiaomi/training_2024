package com.hw.collector.client;

import com.hw.collector.dto.LogEntry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mrk
 * @create 2024-06-06-15:40
 */
@FeignClient(name = "server-service", contextId = "log")
public interface LogClient {
    @PostMapping("/api/log/upload")
    void uploadLogs(@RequestBody List<LogEntry> logs,
                    @RequestParam("storageType") String storageType);
}
