package com.txh.xiaomi2024.work.service.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author txh
 * @Date 2024/6/03 10:04
 * @Desc 日志信息存储表字段信息
 */
@Data // 包含了get，set和toString
@AllArgsConstructor // 有参构造器 set
@NoArgsConstructor // 用于反序列化
public class Log {
    private int id; // 日至id
    private String hostname; // 主机
    private String file; // 日志文件
    private String logs; // 日志列表
    private long last_update_time;

    public Log(String hostname,
               String file,
               String logs,
               long last_update_time) {
        this.hostname = hostname;
        this.file = file;
        this.logs = logs;
        this.last_update_time = last_update_time;
    }
}
