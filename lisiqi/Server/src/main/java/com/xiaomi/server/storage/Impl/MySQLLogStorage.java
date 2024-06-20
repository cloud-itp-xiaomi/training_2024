package com.xiaomi.server.storage.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.mapper.LogEntryMapper;
import com.xiaomi.server.service.LogEntryService;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Component
public class MySQLLogStorage implements LogStorage {

    @Autowired
    private LogEntryMapper logEntryMapper;

    @Override
    public void saveLogEntries(List<LogEntry> logEntries) {
        for (LogEntry logEntry : logEntries) {
            logEntryMapper.insert(logEntry);
        }
    }

    @Override
    public List<LogEntry> getLogEntries(String hostname, String file) {
        QueryWrapper<LogEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hostname", hostname).eq("file", file);
        return logEntryMapper.selectList(queryWrapper);
    }
}
