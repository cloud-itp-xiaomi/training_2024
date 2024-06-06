package com.winter.req;

/**
 * 日志查询对应的实体类
 * */
public class LogQueryReq {
    private String hostname;
    private String file;

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

    @Override
    public String toString() {
        return "LogQueryReq{" +
                "hostname='" + hostname + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
