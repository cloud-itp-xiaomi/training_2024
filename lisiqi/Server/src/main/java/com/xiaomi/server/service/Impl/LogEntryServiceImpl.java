package com.xiaomi.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.mapper.LogEntryMapper;
import com.xiaomi.server.service.LogEntryService;
import com.xiaomi.server.storage.LogStorage;
import com.xiaomi.server.storage.factory.LogStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogEntryServiceImpl extends ServiceImpl<LogEntryMapper, LogEntry> implements LogEntryService {
    public  LogStorage logStorage;

    @Autowired
    public void LogEntryService(LogStorageFactory logStorageFactory) {
        String storageType = "local_file"; // 或 "mysql"
        this.logStorage = logStorageFactory.getLogStorage(storageType);
    }

    public void saveLogEntries(List<LogEntry> logEntries) {
        this.logStorage.saveLogEntries(logEntries);
    }

    public List<LogEntry> getLogEntries(String hostname, String file) {
        return logStorage.getLogEntries(hostname, file);
    }
}

