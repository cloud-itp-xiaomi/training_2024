package com.example.springcloud.service.impl;

import com.example.springcloud.base.BaseJsonUtils;
import com.example.springcloud.base.MyException;
import com.example.springcloud.base.SnowflakeIdGenerator;
import com.example.springcloud.base.enums.ErrorCode;
import com.example.springcloud.base.enums.MetricEnum;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.response.MetricQueryResponse;
import com.example.springcloud.mapper.XmCollectorMapper;
import com.example.springcloud.po.XmCollectorPo;
import com.example.springcloud.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName CollectorServiceImpl
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 23:40
 **/
@Slf4j
@Service
@RefreshScope
public class ServerServiceImpl implements ServerService {
    @Resource
    private XmCollectorMapper xmCollectorMapper;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${redis_config.TTL_Time}")
    private Integer TTL_Time;

    @Override
    @Transactional
    public void metricUpload(MetricUploadRequest request) {
        try {
            Long id = saveToDatabase(request);
            if (id != null) {
                saveToRedis(request);
            } else {
                log.info("保存失败");
            }
        } catch (Exception e) {
            log.error("An error occurred during metric upload: ", e);
        }
    }

    @Override
    public List<MetricQueryResponse> queryMetric(MetricQueryRequest request) {
        if (request.getStart_ts() == null || request.getEnd_ts() == null) {
            throw new MyException(ErrorCode.PARAM_ERROR, "时间参数为空");
        }
        if (Objects.nonNull(request.getMetric())) {
            if (MetricEnum.check(request.getMetric())) {
                throw new MyException(ErrorCode.PARAM_ERROR, "metric参数错误");
            }
        }
        boolean queryFromRedis = shouldQueryFromRedis(request.getStart_ts());
        if (queryFromRedis) {
            log.info("从redis查询");
            return queryFromRedis(request);
        } else {
            log.info("从数据库查询");
            return queryFromDatabase(request);
        }
    }

    private boolean shouldQueryFromRedis(Long startTs) {
        long currentTimeMillis = System.currentTimeMillis();
        long thirtyMinutesAgo = currentTimeMillis - TTL_Time * 60 * 1000;
        return startTs >= thirtyMinutesAgo;
    }

    private List<MetricQueryResponse> queryFromRedis(MetricQueryRequest request) {
        List<XmCollectorPo> poList = selectByMetricAndEndpointAndTimeFromRedis(request.getMetric(), request.getEndpoint(), request.getStart_ts(), request.getEnd_ts());
        Map<String, MetricQueryResponse> resultMap = responseToMap(poList);
        return new ArrayList<>(resultMap.values());
    }

    public List<MetricQueryResponse> queryFromDatabase(MetricQueryRequest request) {
        List<XmCollectorPo> poList = xmCollectorMapper.selectByEndpointAndMetricAndTime(request.getEndpoint(), request.getMetric(), request.getStart_ts(), request.getEnd_ts());
        Map<String, MetricQueryResponse> resultMap = responseToMap(poList);
        return new ArrayList<>(resultMap.values());
    }

    private List<XmCollectorPo> selectByMetricAndEndpointAndTimeFromRedis(String metric, String endpoint, Long start_ts, Long end_ts) {
        String cacheKey = generateQueryKey(metric, endpoint, start_ts, end_ts);
        Set<String> keys = redisTemplate.keys(cacheKey);
        List<XmCollectorPo> poList = keys.stream()
                .map(key1 -> {
                    String json = (String) redisTemplate.opsForValue().get(key1);
                    return json != null ? BaseJsonUtils.readValue(json, XmCollectorPo.class) : null;
                })
                // 过滤掉null值以及不符合时间戳条件的项
                .filter(newPo -> newPo != null && newPo.getTimestamp() >= start_ts && newPo.getTimestamp() <= end_ts)
                .collect(Collectors.toList());
        return poList;
    }

    private String generateQueryKey(String metric, String endpoint, Long start_ts, Long end_ts) {
        String sameTime = getSameTime(String.valueOf(start_ts), String.valueOf(end_ts));
        // 根据条件生成唯一的缓存键
        return "MetricCache:" + metric + ":" + endpoint + ":" + sameTime + "*";
    }

    private String generateUpdateKey(String metric, String endpoint, Long timestamp) {
        // 根据条件生成唯一的缓存键
        return "MetricCache:" + metric + ":" + endpoint + ":" + timestamp;
    }

    public String getSameTime(String st, String et) {
        if (StringUtils.isBlank(st) && StringUtils.isBlank(et)) {
            return null;
        }
        if (st.compareToIgnoreCase(et) > 0) {
            return null; // 开始时间大于结束时间
        }
        int index = 0;
        StringBuilder commonPrefix = new StringBuilder();
        while (index < st.length() && index < et.length()
                && st.charAt(index) == et.charAt(index)) {
            commonPrefix.append(st.charAt(index));
            index++;
        }
        return commonPrefix.toString();
    }

    public Map<String, MetricQueryResponse> responseToMap(List<XmCollectorPo> poList) {
        Map<String, MetricQueryResponse> resultMap = new HashMap<>();
        poList.stream().forEach(po -> {
            String currentMetric = po.getMetric();
            MetricQueryResponse metricResponse = resultMap.computeIfAbsent(currentMetric, k -> {
                MetricQueryResponse newResponse = new MetricQueryResponse();
                newResponse.setMetric(k); // 明确设置metric属性
                return newResponse;
            });
            metricResponse.getValues().add(new MetricQueryResponse.Value(po.getTimestamp(), po.getValue()));
        });
        return resultMap;
    }

    public Long saveToDatabase(MetricUploadRequest request) {
        XmCollectorPo po = new XmCollectorPo();
        long id = snowflakeIdGenerator.nextId();
        po.setId(id);
        po.setEndpoint(request.getEndpoint());
        po.setMetric(request.getMetric());
        po.setStep(request.getStep());
        po.setTimestamp(request.getTimestamp());
        po.setValue(request.getValue());
        xmCollectorMapper.insert(po);
        log.info("\n----------------------\n" + "保存成功:" + po.getEndpoint() + po.getMetric() + po.getTimestamp());
        return id;
    }

    public String saveToRedis(MetricUploadRequest request) {
        String cacheKey = generateUpdateKey(request.getMetric(), request.getEndpoint(), request.getTimestamp());
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            log.info("redis中已存在该key，更新");
            redisTemplate.opsForValue().set(cacheKey, BaseJsonUtils.writeValue(request));
        } else {
            log.info("redis中不存在该key，新增");
            redisTemplate.opsForValue().set(cacheKey, BaseJsonUtils.writeValue(request), TTL_Time, TimeUnit.MINUTES);
        }
        return cacheKey;
    }
}
