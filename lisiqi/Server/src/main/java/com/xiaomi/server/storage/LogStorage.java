package com.xiaomi.server.storage;

import com.xiaomi.server.Entity.LogEntry;

import java.util.List;

public interface LogStorage {
    void store(List<LogEntry> logEntries);
    List<String> query(String hostname, String file);
}
