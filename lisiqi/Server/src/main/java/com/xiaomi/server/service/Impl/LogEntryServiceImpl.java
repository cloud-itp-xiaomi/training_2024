package com.xiaomi.server.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.mapper.LogEntryMapper;
import com.xiaomi.server.service.LogEntryService;
import com.xiaomi.server.storage.LogStorage;
import com.xiaomi.server.storage.factory.LogStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class LogEntryServiceImpl extends ServiceImpl<LogEntryMapper, LogEntry> implements LogEntryService {
    private static final Logger logger = LoggerFactory.getLogger(LogEntryServiceImpl.class);
    private final LogStorageFactory logStorageFactory;

    @Autowired
    public LogEntryServiceImpl(LogStorageFactory logStorageFactory) {
        this.logStorageFactory = logStorageFactory;
    }

    @Override
    public void saveLogEntries(String logStorageType, List<LogEntry> logEntries) {
        logger.info("saveLogEntries called with logStorageType: {}", logStorageType);
        if (logStorageType == null || logStorageType.isEmpty()) {
            logger.error("logStorageType is null or empty");
            throw new IllegalArgumentException("logStorageType is required");
        }

        LogStorage logStorage = logStorageFactory.getLogStorage(logStorageType);
        if (logStorage != null) {
            logStorage.saveLogEntries(logEntries);
            logger.info("Log entries saved successfully using storage type: {}", logStorageType);
        } else {
            logger.error("Invalid log storage type: {}", logStorageType);
            throw new IllegalArgumentException("Invalid log storage type: " + logStorageType);
        }
    }

}
