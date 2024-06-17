package com.lx.collector.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data //自动实现get、set方法
@AllArgsConstructor
@NoArgsConstructor
public class Utilization implements Serializable {

    private String metric; //指标名称
    private String endpoint;//当前主机名称
    private Long timestamp;//采集数据时间
    private Long step = 60L;//采集周期
    private Double value;//指标值

    @Override
    public String toString(){
        return "Utilization{" +
                "metric='" + metric + '\'' +
                "endpoint='" + endpoint + '\'' +
                "timestamp='" + timestamp + '\'' +
                "step='" + step + '\'' +
                "value='" + value +
                '}';
    }
}
