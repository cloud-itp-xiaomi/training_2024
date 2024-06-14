package com.example.demo01.param;

import com.example.demo01.bean.MetricVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MetricResultForm {//查询返回

    private String metric;

    private List<MetricVO> values;

}
