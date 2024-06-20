package com.collector.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cms_collector")
public class CollectorUploadEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 指标名称
    private String metric;

    // 当前主机名称
    private String endpoint;

    // 采集数据时的时间
    private Integer timestamp;

    // 指标的采集周期 默认为60
    private Integer step;

    // 采集到的 CPU 或内存利用率的值，是一个百分比
    private double value;

    // 创建时间
    private Date createTime;

    // 系统类型
    private String systemType;
}
