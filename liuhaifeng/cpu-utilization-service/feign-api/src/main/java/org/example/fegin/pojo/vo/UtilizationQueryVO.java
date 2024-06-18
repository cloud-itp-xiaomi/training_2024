package org.example.fegin.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询cpu内存数据接口返回VO
 *
 * @author liuhaifeng
 * @date 2024/05/30/16:45
 */
@Data
public class UtilizationQueryVO implements Serializable {

    /**
     * 指标名称
     */
    private String metric;

    /**
     * 在指定时间内的指标数据
     */
    private List<Value> values;

    @Data
    public static class Value implements Serializable {
        /**
         * 采集的时间
         */
        private Long timestamp;

        /**
         * 对应的值
         */
        private Double value;
    }
}
