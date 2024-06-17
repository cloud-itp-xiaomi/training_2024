package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.mapper.MetricMapper;
import com.example.xiaomi1.entity.MetricData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricMapper metricMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public MetricService(MetricMapper metricMapper, RedisTemplate<String, Object> redisTemplate) {
        this.metricMapper = metricMapper;
        this.redisTemplate = redisTemplate;
    }

    public void saveMetric(Metric metric) {
        metricMapper.insert(metric);

         // 使用Redis列表存储最近10条数据
        redisTemplate.opsForList().leftPush("metrics", metric);

        if (redisTemplate.opsForList().size("metrics") > 10) {
            redisTemplate.opsForList().trim("metrics", 0, 9);
        }
    }

    // 查询方法
    public List<MetricData> getMetrics(String endpoint, String metric, long start_ts, long end_ts) {
        String redisKey = "metrics";

        try {
            // 尝试从Redis缓存中获取数据
            List<Object> cachedMetrics = redisTemplate.opsForList().range(redisKey, 0, -1);
            List<MetricData> resultMetrics = new ArrayList<>();

            // 处理缓存数据
            if (cachedMetrics != null && !cachedMetrics.isEmpty()) {
                for (Object metricObject : cachedMetrics) {
                    if (metricObject instanceof LinkedHashMap) {
                        Metric cachedMetric = mapToMetric((LinkedHashMap<?, ?>) metricObject);
                        if (isMetricMatch(cachedMetric, endpoint, metric, start_ts, end_ts)) {
                            resultMetrics.add(new MetricData(
                                    cachedMetric.getMetric(),
                                    cachedMetric.getTimestamp(),
                                    cachedMetric.getValue()));
                        }
                    }
                }
            }

            if (resultMetrics.isEmpty()) {
                // 只从MySQL中获取数据
                List<MetricData> dbMetrics = metricMapper.getMetrics(endpoint, metric, start_ts, end_ts);
                System.out.println(dbMetrics);
                resultMetrics.addAll(dbMetrics);

            } else if (resultMetrics.size() == 10) {
                // 缓存的数据全符合要求，在MySQL中查询是否有符合的数据

                // 修改end_ts为缓存中的最早一条数据的时间，假设为(a,b)，Redis可以获取（c,b)之内的所有时间，（a,c）需要在MySQL中查询
                long end_new_ts = resultMetrics.stream().mapToLong(MetricData::getTimestamp).min().orElse(0) - 1;
                List<MetricData> newMetrics = metricMapper.getMetrics(endpoint, metric, start_ts, end_new_ts);
                resultMetrics.addAll(newMetrics);
            }
            return resultMetrics;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    // 数据提取
    private Metric mapToMetric(LinkedHashMap<?, ?> map) {
        Metric cachedMetric = new Metric();
        cachedMetric.setId((Integer) map.get("id"));
        cachedMetric.setMetric((String) map.get("metric"));
        cachedMetric.setEndpoint((String) map.get("endpoint"));
        cachedMetric.setTimestamp(((Integer) map.get("timestamp")).longValue()); // 使用 (long)map.get("timestamp") 报数据类型冲突
        cachedMetric.setStep((Integer) map.get("step"));
        cachedMetric.setValue((Double) map.get("value"));
        return cachedMetric;
    }

    // 筛选数据
    private boolean isMetricMatch(Metric metric, String endpoint, String metricName, long start_ts, long end_ts) {
        return metric.getEndpoint().equals(endpoint) &&
                (metricName == null || metric.getMetric().equals(metricName)) &&
                metric.getTimestamp() >= start_ts &&
                metric.getTimestamp() <= end_ts;
    }
}
