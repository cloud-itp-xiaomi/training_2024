package com.xiaomi.server.storage;

import com.xiaomi.server.Entity.LogEntry;

import java.util.List;

public interface LogStorage {
    void saveLogEntries(List<LogEntry> logEntries);
    List<LogEntry> getLogEntries(String hostname, String file);
}
