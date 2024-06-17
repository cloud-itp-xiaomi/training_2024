package com.jiuth.sysmonitorcapture.storage;


import com.jiuth.sysmonitorcapture.dao.LogEntry;
import com.jiuth.sysmonitorcapture.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jiuth
 */
@Service
public class MySqlLogStorageService implements LogStorageService {

    @Autowired
    private LogMapper logMapper;


    @Override
    public void storeLogs(Map<String, List<String>> fileLogs) throws Exception {
        fileLogs.forEach((file, logs) -> logs.forEach(log -> {
            LogEntry logEntry = new LogEntry();
            logEntry.setHostname("my-computer");
            logEntry.setFile(file);
            logEntry.setLog(log);
            logMapper.insertLog(logEntry);
        }));
    }
}