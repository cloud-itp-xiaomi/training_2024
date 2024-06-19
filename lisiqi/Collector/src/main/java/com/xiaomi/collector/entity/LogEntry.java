package com.xiaomi.collector.entity;

import java.util.List;


public class LogEntry {
    private String hostname;
    private String file;
    private List<String> logs;

    public LogEntry() {}

    public LogEntry(String hostname, String file, List<String> logs) {
        this.hostname = hostname;
        this.file = file;
        this.logs = logs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

}
