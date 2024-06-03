package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.mapper.MetricMapper;
import com.example.xiaomi1.entity.MetricData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@Service
public class MetricService {

    @Autowired
    private MetricMapper metricMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

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

        // System.out.println("metric:" + metric);
        try {
            // 尝试从Redis缓存中获取数据
            List<Object> cachedMetrics = redisTemplate.opsForList().range(redisKey, 0, -1);
            // System.out.println(cachedMetrics);
            List<MetricData> resultMetrics = new ArrayList<>();

            // 处理缓存数据
            if (cachedMetrics != null && !cachedMetrics.isEmpty()) {
                for (Object metricObject : cachedMetrics) {
                    // System.out.println("metricObject:" + metricObject);

                    if (metricObject instanceof LinkedHashMap) {
                        Metric cachedMetric = mapToMetric((LinkedHashMap<?, ?>) metricObject);
                        // System.out.println("cachedMetric" + cachedMetric);
                        if (isMetricMatch(cachedMetric, endpoint, metric, start_ts, end_ts)) {
                            resultMetrics.add(new MetricData(
                                    cachedMetric.getMetric(),
                                    cachedMetric.getTimestamp(),
                                    cachedMetric.getValue()));
                        }
                    }
                }
            }
            // System.out.println(resultMetrics);

            if (resultMetrics.isEmpty()) {
                // System.out.println("redis中数据为空");

                // 从mysql中获取数据
                List<MetricData> dbMetrics = metricMapper.getMetrics(endpoint, metric, start_ts, end_ts);
                resultMetrics.addAll(dbMetrics);
                // 将查询结果缓存到Redis，设置过期时间为1小时
                // redisTemplate.opsForValue().set(redisKey, dbMetrics, 1, TimeUnit.HOURS);
            } else if (resultMetrics.size() == 10) {
                // 缓存的数据全符合要求，在mysql中查询是否有符合的数据
                // System.out.println("redis和mysql均查询了");

                // 修改end_ts为缓存中的最早一条数据的时间，假设为(a,b)，redis可以获取（c,b)之内的所有时间，（a,c）需要在mysql中查询
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
