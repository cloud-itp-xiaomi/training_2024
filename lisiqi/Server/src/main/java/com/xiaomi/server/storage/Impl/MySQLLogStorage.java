package com.xiaomi.server.storage.Impl;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MySQLLogStorage implements LogStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void store(List<LogEntry> logEntries) {
        for (LogEntry entry : logEntries) {
            String sql = "INSERT INTO logs (hostname, file, log) VALUES (?, ?, ?)";
            for (String log : entry.getLogs()) {
                jdbcTemplate.update(sql, entry.getHostname(), entry.getFile(), log);
            }
        }
    }

    @Override
    public List<String> query(String hostname, String file) {
        String sql = "SELECT log FROM logs WHERE hostname = ? AND file = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{hostname, file}, String.class);
    }
}
