package com.winter.domain;


/**
 * 保存在MySQL数据对应的实体类
 * */
public class LogData {
    private String id;  //主键id
    private String hostname;  //主机名
    private String file;  //日志采集文件所在的全路径
    private String logs;  //具体的日志内容, 每一条日志使用","分开

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "LogData{" +
                "id='" + id + '\'' +
                ", hostname='" + hostname + '\'' +
                ", file='" + file + '\'' +
                ", logs='" + logs + '\'' +
                '}';
    }
}
