package com.winter.resp;

import java.util.List;

/**
 * 查询的日志信息的响应结果对应的实体类
 * */
public class LogDataResp {
    private String hostname;
    private String file;
    private List<String> logs;

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

    @Override
    public String toString() {
        return "LogDataResp{" +
                "hostname='" + hostname + '\'' +
                ", file='" + file + '\'' +
                ", logs=" + logs +
                '}';
    }
}
