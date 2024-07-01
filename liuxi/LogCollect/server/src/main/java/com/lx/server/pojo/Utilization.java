package com.lx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data //自动实现get、set方法
@AllArgsConstructor
@NoArgsConstructor
/**
 * 主机利用率
 */
public class Utilization implements Serializable {

    private Long id;//id号
    private String metric; //指标名称
    private String endpoint;//当前主机名称
    private Long timestamp;//采集数据时间
    private Long step;//采集周期
    private Double value;//指标值

    @Override
    public String toString() {
        return "Utilization{" +
                "id='" + id + '\'' +
                "metric='" + metric + '\'' +
                "endpoint='" + endpoint + '\'' +
                "timestamp='" + timestamp + '\'' +
                "step='" + step + '\'' +
                "value='" + value +
                '}';
    }
}
