package com.collector.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.collector.bean.entity.MetircUploadEntity;
import com.collector.bean.request.MetircQueryRequest;
import com.collector.bean.request.MetircUploadRequest;
import com.collector.bean.response.MetricResponse;
import com.collector.cache.CacheClient;
import com.collector.utils.Common;
import com.collector.mapper.MetricMapper;
import com.collector.service.IMetricService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricService extends ServiceImpl<MetricMapper, MetircUploadEntity> implements IMetricService {
    @Resource
    private MetricMapper metricMapper;
    @Resource
    private Common common;
    @Resource
    private CacheClient cacheClient;

    public static final String REDIS_KEY_PREFIX = "COLLECTOR:KEY"; // 前缀

    @Override
    public void upload(List<MetircUploadRequest> requests) {
        List<MetircUploadEntity> list = new ArrayList<>();
        requests.forEach(q -> {
            MetircUploadEntity entity = new MetircUploadEntity();
            // 用于将q对象的属性值复制到entity对象中。该方法会自动匹配两个对象中相同名称的属性，并进行赋值操作，浅拷贝
            BeanUtils.copyProperties(q, entity);
            entity.setCreateTime(new Date());
            entity.setSystemType(common.getSystemType());
            list.add(entity);

            // 存入数据库,com.baomidou.mybatisplus.core.mapper.BaseMapper<T>中的函数
            metricMapper.insert(entity);
        });

        // 存入redis，避免用参数控制逻辑，在传参前判断
        list.forEach(q -> {
            String value = JSONUtil.toJsonStr(q);
            if(!StringUtils.isEmpty(value)){
                cacheClient.saveLatestData(REDIS_KEY_PREFIX, value);
            }
        }
        );
    }

    @Override
    public List<MetricResponse> queryMetricInfo(MetircQueryRequest request) {
        List<MetricResponse> metricResponse = new ArrayList<>();

        // 查询接口优先取redis中数据,如查询不到再去查数据库
        // 先获得redis中的数据，转为对象列表
        List<MetircUploadEntity> list = new ArrayList<>();
        List<Object> latestData = cacheClient.getLatestData(REDIS_KEY_PREFIX);
        latestData.forEach(obj -> {
            MetircUploadEntity bean = JSONUtil.toBean(String.valueOf(obj), MetircUploadEntity.class);
            list.add(bean);
        });
        // 筛选符合条件的数据 filter（），返回新数组，常与steam一起使用，注意结尾要结束流即.collect(Collectors.toList())
        List<MetircUploadEntity> collect = list.stream().filter(q ->
                Objects.equals(q.getEndpoint(), request.getEndpoint())
                        && Objects.equals(q.getMetric(), request.getMetric())
                        && q.getTimestamp() >= request.getStart_ts()
                        && q.getTimestamp() <= request.getEnd_ts()).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            getCollectorResponse(collect, metricResponse);
            return metricResponse;
        }

        // 在数据库查，LambdaQueryWrapper是MyBatis-Plus提供的一种查询构建器，相当于sql语句
        // 通过Lambda表达式来构建查询条件，使用起来比传统的SQL语句更加简洁和易读
        LambdaQueryWrapper<MetircUploadEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MetircUploadEntity::getEndpoint, request.getEndpoint());
        if(StringUtils.isNotBlank(request.getMetric())){
            // metric不为空的时候 才加查询条件
            lambdaQueryWrapper.eq(MetircUploadEntity::getMetric, request.getMetric());
        }
        lambdaQueryWrapper.between(MetircUploadEntity::getTimestamp, request.getStart_ts(), request.getEnd_ts());

        List<MetircUploadEntity> collectorUploadEntities = metricMapper.selectList(lambdaQueryWrapper);
        getCollectorResponse(collectorUploadEntities, metricResponse);

        return metricResponse;
    }

    // 根据符合条件的数据库信息，返回需要的信息对象
    private static void getCollectorResponse(List<MetircUploadEntity> collectorUploadEntities, List<MetricResponse> metricRespons) {
        // 分组查询
        // 依据是每个实体的metric和endpoint拼接成的字符串，利用Collectors.groupingBy方法生成一个Map，其中键是分组标识，值是属于该组的实体列表
        Map<String, List<MetircUploadEntity>> map = collectorUploadEntities.stream()
                .collect(Collectors.groupingBy(o -> o.getMetric() + "-" + o.getEndpoint(), Collectors.toList()));

        // 遍历map的键，获取每个分组的实体列表，并构建CollectorResponse对象
        map.keySet().forEach(q -> {
            MetricResponse response = new MetricResponse();

            // 每个分组的metric值是一样的，取第一个即可
            List<MetircUploadEntity> mapList = map.get(q);
            response.setMetric(mapList.get(0).getMetric());

            List<MetricResponse.Values> values = new ArrayList<>();
            mapList.forEach(tt -> {
                MetricResponse.Values value = new MetricResponse.Values();
                value.setValue(tt.getValue());
                value.setTimestamp(tt.getTimestamp());
                values.add(value);
            });
            response.setValues(values);

            metricRespons.add(response);
        });
    }
}
