package com.lx.server.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 消费消息获得的对象
 */
@Data
public class LogMessage implements Serializable {
    private String hostName;  //采集主机名
    private String file;  //文件路径
    private List<String> logs;  //日志内容

    @Override
    public String toString() {
        return "Log{" +
                "hostName= \"" + hostName + '\"' + '\n' +
                "file= \"" + file +  '\"'  + '\n' +
                '}';
    }
}
