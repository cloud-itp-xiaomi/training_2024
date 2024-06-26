package com.h_s.service.Impl;

import com.h_s.entity.LogEntry;
import com.h_s.entity.LogEntryLogs;
import com.h_s.entity.LogEntryRepository;
import com.h_s.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MysqlLogServiceImpl implements LogService {
    private final LogEntryRepository logEntryRepository;

    @Autowired
    public MysqlLogServiceImpl(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Override
    public void saveLogs(String hostname, String file, List<String> logs) {
        LogEntry logEntry = new LogEntry();
        logEntry.setHostname(hostname);
        logEntry.setFile(file);

        List<LogEntryLogs> logEntryLogs = logs.stream()
                .map(log -> new LogEntryLogs(null, logEntry, log))
                .collect(Collectors.toList());

        logEntry.setLogs(logEntryLogs);
        logEntryRepository.save(logEntry);
    }

    @Override
    public List<Map<String, String>> queryLogs(String hostname, String file) {
        List<LogEntry> logEntries = logEntryRepository.findByHostnameAndFile(hostname, file);
        return logEntries.stream()
                .flatMap(logEntry -> logEntry.getLogs().stream())
                .map(logEntryLogs -> Map.of(
                        "hostname", logEntryLogs.getLogEntry().getHostname(),
                        "file", logEntryLogs.getLogEntry().getFile(),
                        "logs", logEntryLogs.getLogContent()
                ))
                .collect(Collectors.toList());
    }
}
