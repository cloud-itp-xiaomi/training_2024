package com.example.xiaomi1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "log")
public class Log {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String hostname;
    private String file;
    private String log;

    @Override
    public String toString() {
        return "Log{" +
                "id='" + id + '\'' +
                ", hostname='" + hostname + '\'' +
                ", file='"+file+'\''+
                ", log='"+log+'\''+
                '}';
    }

    // 返回格式化的字符串
    public String toFormattedString() {
        return "hostname:" + hostname + ", file:" + file + ", log:" + log;
    }
}
