package com.example.hostmonitor.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author: WangYF
 * @Date: 2024/05/31
 * @Description: 按metric分类查询结果的字段
 */
@Data
@AllArgsConstructor
public class QueryData {
    private String metric;
    private List<TimeWithValue> values;

    @Data
    @AllArgsConstructor
    public static class TimeWithValue{
        private Long timestamp;
        private Double value;
    }
}
