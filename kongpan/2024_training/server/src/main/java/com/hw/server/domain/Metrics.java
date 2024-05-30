package com.hw.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mrk
 * @create 2024-05-22-14:48
 */
@Data
@TableName("metrics")
public class Metrics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 指标名称
     */
    private String metric;

    /**
     * 主机名称
     */
    private String endpoint;

    /**
     * 采集数据的时间
     */
    private Long timestamp;

    /**
     * 采集周期
     */
    private Long step;

    /**
     * 指标值
     */
    private Double value;

}
