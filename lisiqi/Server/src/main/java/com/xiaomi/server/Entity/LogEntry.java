package com.xiaomi.server.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaomi.server.config.ListToStringTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;

import java.util.List;

@Data
@TableName("logs")
public class LogEntry {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String hostname;
    private String file;

    @TableField(typeHandler = ListToStringTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<String> logs;

    private Timestamp timestamp;
}
