package com.example.hostmonitor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: WangYF
 * @Date: 2024/05/31
 * @Description: 数据表的统一字段
 */
@Data
@TableName(value = "resource_usage", autoResultMap = true)
public class HostResourceUsageEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("metric")
    private String metric;
    @TableField("endpoint")
    private String endpoint;
    @TableField("timestamp")
    private Long timestamp;
    @TableField("value")
    private Double value;
}
