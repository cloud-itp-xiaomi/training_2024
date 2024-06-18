package com.xiaomi.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomi.server.Entity.LogEntry;

import java.util.List;

public interface LogEntryService extends IService<LogEntry> {
    void saveLogEntries(List<LogEntry> logEntries);
}

