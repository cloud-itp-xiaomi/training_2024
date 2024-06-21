package com.txh.xiaomi2024.work.service.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author txh
 * @Date 2024/5/31 17:04
 * @Desc 资源利用率存储表字段信息
 */
@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor // 用于反序列化
public class Utilization {
    private int id;
    private String metric;
    private String endpoint;
    private long collect_time;
    private int step;
    private double value_metric;
    // mapper检索数据库，需要这两个有参构造函数进行检索得到需要的字段
    public Utilization(String metric,
                       String endpoint,
                       long collect_time,
                       int step,
                       double value_metric) {
        this.metric = metric;
        this.endpoint = endpoint;
        this.collect_time = collect_time;
        this.step = step;
        this.value_metric = value_metric;
    }
    public Utilization(String metric,
                       long collect_time,
                       double value_metric) {
        this.metric = metric;
        this.collect_time = collect_time;
        this.value_metric = value_metric;
    }
}
