package org.xiaom.yhl.server.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xiaom.yhl.server.server.entity.Metric;
import org.xiaom.yhl.server.server.repository.MetricRepository;

import java.util.List;

/**
 * ClassName: MetricService
 * Package: org.xiaom.yhl.server.server.service
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/30 15:32
 * @Version 1.0
 */
@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CPU_RECENT_METRICS_KEY = "recentCpuMetrics";
    private static final String MEM_RECENT_METRICS_KEY = "recentMemMetrics";

    public void saveMetric(Metric metric) {
        metricRepository.save(metric);

        String redisKey = metric.getMetric().equals("cpu.used.percent") ? CPU_RECENT_METRICS_KEY : MEM_RECENT_METRICS_KEY;

        redisTemplate.opsForList().leftPush(redisKey, metric);
        if (redisTemplate.opsForList().size(redisKey) > 10) {
            redisTemplate.opsForList().rightPop(redisKey);
        }
    }

    public List<Metric> queryMetrics(String endpoint, String metric, long startTs, long endTs) {
        return metricRepository.findByEndpointAndMetricAndTimestampBetween(endpoint, metric, startTs, endTs);
    }
}
