package com.hw.server.controller;


import com.hw.server.domain.dto.Result;
import com.hw.server.domain.po.Logs;
import com.hw.server.factory.LogStorageFactory;
import com.hw.server.service.ILogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mrk
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogsController {

//    private final ILogsService logsService;
    private final LogStorageFactory logStorageFactory;

    @PostMapping("/upload")
    public Result<?> uploadLogs(@RequestBody List<Logs> logs,
                                @RequestParam("storageType") String storageType) {
        ILogsService logsService = logStorageFactory.getLogStorage(storageType);
        return logsService.uploadLogs(logs);
    }

    @GetMapping("/query")
    public Result<?> queryLogs(@RequestParam("hostname") String hostname,
                               @RequestParam("file") String file,
                               @RequestParam("storageType") String storageType) {
        ILogsService logsService = logStorageFactory.getLogStorage(storageType);
        return logsService.queryLogs(hostname, file);
    }

}
