package org.qiaojingjing.server.pojo.dto;

import lombok.Data;

/**
 * 接收上传指标DTO
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/

@Data
public class MetricsDTO {
    private String metric; //指标名称
    private String endpoint; //当前主机名称
    private Long timestamp; //采集数据的时间
    private Long step; //指标的采集周期(固定为60)
    private Double value; //采集到的CPU 或内存利用率的值
}
