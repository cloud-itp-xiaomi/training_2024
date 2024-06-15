package com.lx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 存储在log表中的日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMysql implements Serializable {

    private int id;//自增主键
    private String hostName;//主机名
    private String file;//对应日志文件
    private String log;//日志

    @Override
    public String toString() {
        return "LogMysql{" +
                "id='" + id + '\'' +
                "hostName='" + hostName + '\'' +
                "file='" + file + '\'' +
                "log='" + log + '\'' +
                '}';
    }
}
