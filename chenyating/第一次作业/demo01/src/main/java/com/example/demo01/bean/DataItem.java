package com.example.demo01.bean;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DataItem implements Serializable {
    // 是否删除
    private Integer isDeleted;
    // 创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //创建用户Id
    private String createId;
    // 主机名称
    private String endpoint;
    //指标名称
    private String metricName;
    //指标数值
    private BigDecimal percentValue;
    //采集频率(单位s)
    private long step;
    //采集时间
    private long timestamp;
}
