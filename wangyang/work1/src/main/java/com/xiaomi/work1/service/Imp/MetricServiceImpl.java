package com.xiaomi.work1.service.Imp;


import com.alibaba.fastjson.JSON;
import com.xiaomi.work1.bean.Metric;
import com.xiaomi.work1.bean.Result;
import com.xiaomi.work1.mapper.MetricMapper;
import com.xiaomi.work1.service.MetricService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * ClassName: MetricServiceImpl
 * Package: com.xiaomi.work1.service.Imp
 * Description:服务实现类
 *
 * @Author WangYang
 * @Create 2024/5/24 22:06
 * @Version 1.0
 */
@Service
public class MetricServiceImpl implements MetricService {
    public static final String CACHE_METRIC= "cache:metrics";
    @Resource
    private MetricMapper metricMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result<?> uploadMetrics(List<Metric> metrics) {
        //数据处理
        for (Metric metric : metrics) {
            //1.将数据保存的数据库
            metricMapper.saveMetrics(metric);
            //2.将数据缓存到redis
            stringRedisTemplate.opsForZSet().add(CACHE_METRIC, JSON.toJSONString(metric),System.currentTimeMillis());
            int count = stringRedisTemplate.opsForZSet().zCard(CACHE_METRIC).intValue();
            if(count>10){
                stringRedisTemplate.opsForZSet().removeRange(CACHE_METRIC,0,count-10-1);
            }
        }
        //保存成功，返回{200,"ok",null}
        return Result.Ok();
    }

    @Override
    public Result<?> queryMetrics(String endpoint, String metric, Long startTs, Long endTs) {
        //查询数据
        List<Metric> metricList=metricMapper.queryAll(endpoint,metric,startTs,endTs);

        return Result.Ok(metricList);
    }
}
