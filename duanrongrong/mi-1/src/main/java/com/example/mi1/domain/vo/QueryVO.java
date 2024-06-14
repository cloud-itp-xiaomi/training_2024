package com.example.mi1.domain.vo;

import lombok.Data;

import java.util.List;


@Data
public class QueryVO {
    private String metric;
    private List<QueryValue> values;
    private Integer pageNum;
    private Integer totalNum;
}
