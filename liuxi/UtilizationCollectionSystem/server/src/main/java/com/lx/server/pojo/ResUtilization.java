package com.lx.server.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
/*
将返回的多个Utilization对象封装成ResUtilization对象
*/
public class ResUtilization implements Serializable {

    private String metric;
    private List<Map<String, Object>> values;

    public ResUtilization() {
    }

    //为value属性添加元素
    public void addValue(Utilization utilization) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("timestamp" , utilization.getTimestamp());
        entry.put("value" , utilization.getValue());
        values.add(entry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("metric: \"").append(metric).append('\"');
        sb.append(", values: ").append(values);
        sb.append('}');
        return sb.toString();
    }

}
