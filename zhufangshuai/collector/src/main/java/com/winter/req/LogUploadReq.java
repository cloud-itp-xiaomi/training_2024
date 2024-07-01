package com.winter.req;


import java.util.List;

/**
 * 日志上报对应的Request
 * */
public class LogUploadReq {
    private String hostname;  //主机名
    private String file;  //日志采集文件所在的全路径
    private List<String> logs;  //具体的日志内容

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
        return "LogUploadReq{" +
                "hostname='" + hostname + '\'' +
                ", file='" + file + '\'' +
                ", logs=" + logs +
                '}';
    }
}
