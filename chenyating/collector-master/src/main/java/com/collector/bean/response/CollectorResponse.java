package com.collector.bean.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CollectorResponse implements Serializable {
    // 一个序列化版本号，它用于标识一个特定类的序列化版本。用于序列化和反序列化
    private static final long serialVersionUID = 2L;

    private String metric;

    private List<Values> values;

    @Data
    public static class Values {

        private Integer timestamp;

        private double value;
    }
}
