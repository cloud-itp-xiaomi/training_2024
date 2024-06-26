package com.h_s.service.Impl;

import com.h_s.entity.Metric;
import com.h_s.entity.MetricRepository;
import com.h_s.mapper.MetricMapper;
import com.h_s.service.MetricService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricServiceImpl implements MetricService {

    @Autowired
    private final MetricRepository metricRepository;
    @Autowired
    private final MetricMapper metricMapper;
    @Resource
    private RedisTemplate<String, Metric> redisTemplate;

    public MetricServiceImpl(MetricRepository metricRepository, MetricMapper metricMapper) {
        this.metricRepository = metricRepository;
        this.metricMapper = metricMapper;
    }

    @Override
    public void saveMetrics(List<Metric> metrics) {
        for (Metric metric : metrics) {
            metricRepository.save(metric);
            String key = metric.getEndpoint() + ":" + metric.getMetric();
            redisTemplate.opsForList().rightPush(key, metric);
            if (redisTemplate.opsForList().size(key) > 10) {
                redisTemplate.opsForList().leftPop(key);
            }
        }
    }

    //查询使用JPA自动仓库方式实现findByName（符合格式需求）
    @Override
    public List<Metric> getMetrics(String endpoint, String metric, long start_ts, long end_ts) {
        if (metric == null || metric.isEmpty()) {
//            return metricRepository.findByEndpointAndTimestampBetween(endpoint, start_ts, end_ts);
            return metricMapper.queryMapperMetrics(endpoint, start_ts, end_ts);
        } else {
//            return metricRepository.findByEndpointAndMetricAndTimestampBetween(endpoint, metric, start_ts, end_ts);
            return metricMapper.queryMapperMetricsAll(endpoint, metric, start_ts, end_ts);
        }
    }
}
