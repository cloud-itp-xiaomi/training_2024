package com.test.lab1.service;

import com.test.lab1.entity.Metric;
import com.test.lab1.mapper.MetricMapper;
import com.test.lab1.entity.MetricData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MetricService {

    @Autowired
    private MetricMapper metricMapper;

    /*
    * 保存方法
    * */
    public void saveMetric(Metric metric) {
        metricMapper.insert(metric);
    }

    /*
     * 查询方法
     * */
    public List<MetricData> getMetrics(String endpoint, String metric, long start_ts, long end_ts) {
        //String redisKey = "entitys";
        return metricMapper.getMetrics(endpoint, metric, start_ts, end_ts);
    }

}
