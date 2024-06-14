package com.example.demo01.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MetricVO {//查询返回的values部分
    @JsonIgnore
    private String metric;

    private Long timestamp;

    private BigDecimal value;

}
