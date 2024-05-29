package org.qiaojingjing.server.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
    private String metric; //指标名称,CPU或者内存使用率
    private String endpoint; //当前主机名称
    private Long timestamp; //采集数据的时间
    private Long step; //指标的采集周期(固定为60)
    private Double value; //采集到的CPU 或内存利用率的值
    private Map<String,String> tags;
}
