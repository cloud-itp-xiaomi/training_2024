package com.hw.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hw.server.domain.po.Metrics;
import com.hw.server.domain.dto.MetricDTO;
import com.hw.server.domain.dto.Result;
import com.hw.server.domain.dto.ValueDTO;
import com.hw.server.mapper.MetricsMapper;
import com.hw.server.service.IMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

import static com.hw.server.constants.RedisConstants.CACHE_METRIC_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrk
 * @since 2024-05-22
 */
@Service
@RequiredArgsConstructor
public class MetricsServiceImpl extends ServiceImpl<MetricsMapper, Metrics> implements IMetricsService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final int THRESHOLD = 10;

    @Override
    public Result<?> uploadMetrics(@RequestBody List<Metrics> metrics) {
        try {
            for (Metrics metric : metrics) {
                // 1. 将数据存入 mysql 数据库
                this.save(metric);
                // 2. 将数据存入 redis 缓存，缓存最近的十条数据
                stringRedisTemplate.opsForZSet().add(CACHE_METRIC_KEY, JSONUtil.toJsonStr(metric), metric.getTimestamp());
                int nums = stringRedisTemplate.opsForZSet().zCard(CACHE_METRIC_KEY).intValue();
                if (nums > THRESHOLD) {
                    stringRedisTemplate.opsForZSet().removeRange(CACHE_METRIC_KEY, 0, nums - THRESHOLD - 1);
                }
            }
            return Result.ok();
        } catch (Exception e) {
            return Result.error("Data upload failed");
        }
    }

    @Override
    public Result<?> queryMetrics(String endpoint, String metric, Long startTs, Long endTs) {
        // 1.从 redis 中查询数据，并判断缓存是否命中
        Set<String> jsonStrSet = stringRedisTemplate.opsForZSet().rangeByScore(CACHE_METRIC_KEY, startTs, endTs);
        if (CollectionUtil.isNotEmpty(jsonStrSet)) {
            // 1.1 缓存命中，将 json 字符串反序列化为 Metric 对象列表
            List<Metrics> cacheMetrics = jsonStrSet.stream()
                    .map(jsonStr -> JSONUtil.toBean(jsonStr, Metrics.class))
                    .collect(Collectors.toList());
            // 1.2 根据 endpoint 和 metric 进行进一步过滤
            List<Metrics> cacheMetricList = cacheMetrics.stream()
                    .filter(cacheMetric -> cacheMetric.getEndpoint().equals(endpoint) &&
                            (cacheMetric.getMetric().equals(metric) || metric == null))
                    .collect(Collectors.toList());
            // 1.3 转为指定格式
            List<MetricDTO> cacheMetricDTOList = formatTrans(cacheMetricList);
            return Result.ok(cacheMetricDTOList);
        }
        // 2. 缓存未命中，从数据库查询，并判断是否命中
        try {
            LambdaQueryWrapper<Metrics> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(Metrics::getEndpoint, endpoint)
                    .between(Metrics::getTimestamp, startTs, endTs);

            if (StrUtil.isNotBlank(metric)) {
                queryWrapper.eq(Metrics::getMetric, metric);
            }
            List<Metrics> metricsList = this.list(queryWrapper);

            // 2.1 数据库中不存在，返回失败信息
            if (CollectionUtil.isEmpty(metricsList)) {
                return Result.error("Data not exist");
            }
            // 2.2 数据库中存在，返回查询数据
            List<MetricDTO> metricDTOList = formatTrans(metricsList);
            return Result.ok(metricDTOList);

        } catch (Exception e) {
            return Result.error("Database query failed");
        }
    }

    public List<MetricDTO> formatTrans(List<Metrics> metricsList) {
        Map<String, List<ValueDTO>> map = new HashMap<>();

        for (Metrics metrics : metricsList) {
            String metric = metrics.getMetric();
            Long timestamp = metrics.getTimestamp();
            Double value = metrics.getValue();

            ValueDTO valueDTO = new ValueDTO(timestamp, value);

            if (!map.containsKey(metric)) {
                map.put(metric, new ArrayList<>());
            }
            map.get(metric).add(valueDTO);
        }

        List<MetricDTO> res = new ArrayList<>();
        for (Map.Entry<String, List<ValueDTO>> entry : map.entrySet()) {
            String metric = entry.getKey();
            List<ValueDTO> value = entry.getValue();
            MetricDTO metricDTO = new MetricDTO(metric, value);
            res.add(metricDTO);
        }
        return res;
    }

}
