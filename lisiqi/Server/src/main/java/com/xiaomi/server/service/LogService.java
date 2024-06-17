package com.xiaomi.server.service;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.storage.LogStorage;
import com.xiaomi.server.storage.factory.LogStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogStorageFactory logStorageFactory;

    public void storeLogs(List<LogEntry> logEntries) {
        LogStorage logStorage = logStorageFactory.getLogStorage();
        logStorage.store(logEntries);
    }

    public List<String> queryLogs(String hostname, String file) {
        LogStorage logStorage = logStorageFactory.getLogStorage();
        return logStorage.query(hostname, file);
    }
}
