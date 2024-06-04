package com.xiaomi.work1.bean;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.lang.annotation.Target;

/**
 * ClassName: Metric
 * Package: com.xiaomi.work1.bean
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/24 21:39
 * @Version 1.0
 */
@Data
@Table(name="metrics")
public class Metric implements Serializable {
    private static final  long serializableUID=1L;
    /**
     * 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    /**
     * 指标名称
     */
    private  String metric;

    /**
     *当前主机名称
     */
    private  String endpoint;

    /**
     *采集数据时的时间
     */
    private Long timestamp;

    /**
     *指标的采集周期
     */
    private Long step;
    /**
     *采集到的CPU或内存利
     * ⽤率的值
     */
    private Double value;

    public Metric(){

    }

    public Metric(String metric, String endpoint, Long timestamp, Long step, Double value) {
        this.metric = metric;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.step = step;
        this.value = value;
    }
}
