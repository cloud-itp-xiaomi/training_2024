package com.collector.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("collector_log")
public class LogUploadEntity {
    // 主键
    @TableId(type = IdType.AUTO)
    private int id;

    // 当前主机名称
    private String hostname;

    // 日志文件的全路径
    private String file;

    // 创建时间
    private Date createTime;

    // 系统类型
    private String systemType;

    // 日志文件内容，每个元素是一条日志
    // 用于标注一个字段是否存在于数据库表中
    @TableField(exist = false)
    private String[] logs;
}
