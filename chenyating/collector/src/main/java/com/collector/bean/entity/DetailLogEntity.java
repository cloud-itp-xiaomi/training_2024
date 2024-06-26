package com.collector.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

//日志详细记录表
@Data
@TableName("collector_detail_log")
public class DetailLogEntity {
    private Integer logId;

    private String logs;

    private Date createTime;
}
