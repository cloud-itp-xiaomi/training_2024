package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * cpu和内存利用率信息表
 *
 * @author liuhaifeng
 * @date 2024/05/29/17:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Utilization {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 指标名称
     */
    private String metric;

    /**
     * 主机id
     */
    private Integer endpointId;

    /**
     * 采集数据时的时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 指标采集周期
     */
    private Integer step;

    /**
     * 内存或cpu利用率
     */
    private Double value;

    /**
     * 指标类型
     */
    private Integer metricType;

    /**
     * 保留字段
     */
    private String tags;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
