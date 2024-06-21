package org.example.fegin.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传cpu内存数据接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/05/30/0:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilizationUploadDTO {

    /**
     * 指标名称
     */
    private String metric;

    /**
     * 主机名称
     */
    private String endpoint;

    /**
     * 采集数据时的时间
     */
    private Long timestamp;

    /**
     * 指标的采集周期，固定60s
     */
    private Integer step;

    /**
     * 采集到的CPU或内存利⽤率的值，是⼀个百分⽐，如60.1代表利⽤率为60.1%
     */
    private Double value;

    /**
     * 保留字段
     */
    private String tags;
}
