package com.xiaomi.server.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Map;

@Data
@TableName("Metric")
public class Metric
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private Double value;

    @TableField(exist = false)
    private Map<String, String> tags;
}