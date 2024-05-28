package com.xiaomi.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomi.server.Entity.Metric;
import com.xiaomi.server.mapper.MetricMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricService extends ServiceImpl<MetricMapper, Metric> {
    @Autowired
    private MetricMapper metricMapper;

    public List<Metric> queryMetrics(String endpoint, String metric, long startTs, long endTs) {
        return metricMapper.queryMetrics(endpoint, metric, startTs, endTs);
    }

}
