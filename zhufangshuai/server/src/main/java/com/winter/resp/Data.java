package com.winter.resp;

import java.util.List;

/**
 * 用户查询时封装在content中的内容：data
 * */
public class Data {
    private String metric;  //指标名称
    List<Values> values;  //采集到的数据

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public List<Values> getValues() {
        return values;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }
}
