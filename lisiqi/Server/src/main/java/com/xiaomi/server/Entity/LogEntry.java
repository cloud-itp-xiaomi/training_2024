package com.xiaomi.server.Entity;

import lombok.Data;

import java.util.List;

@Data
public class LogEntry {
    private String hostname;
    private String file;
    private List<String> logs;
}
