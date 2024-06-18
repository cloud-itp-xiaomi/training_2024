package com.collector.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.collector.bean.entity.CollectorUploadEntity;
import com.collector.bean.request.CollectorQueryRequest;
import com.collector.bean.request.CollectorUploadRequest;
import com.collector.bean.response.CollectorResponse;
import com.collector.cache.CacheClient;
import com.collector.utils.Common;
import com.collector.utils.RedisConstants;
import com.collector.mapper.CollectorMapper;
import com.collector.service.ICollectorService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectorServiceImpl extends ServiceImpl<CollectorMapper, CollectorUploadEntity> implements ICollectorService {
    @Resource
    private CollectorMapper collectorMapper;
    @Resource
    private Common common;
    @Resource
    private CacheClient cacheClient;

    @Override
    public Boolean upload(List<CollectorUploadRequest> requests) {
        List<CollectorUploadEntity> list = new ArrayList<>();
        requests.forEach(q -> {
            CollectorUploadEntity entity = new CollectorUploadEntity();
            // 用于将q对象的属性值复制到entity对象中。
            // 该方法会自动匹配两个对象中相同名称的属性，并进行赋值操作，浅拷贝
            BeanUtils.copyProperties(q, entity);
            entity.setCreateTime(new Date());
            entity.setSystemType(common.getSystemType());
            list.add(entity);

            // 存入数据库
            // com.baomidou.mybatisplus.core.mapper.BaseMapper<T>中的函数
            collectorMapper.insert(entity);
        });

        // 存入redis
        list.forEach(q -> cacheClient.saveLatestData(RedisConstants.REDIS_KEY_PREFIX, JSONUtil.toJsonStr(q)));

        return Boolean.TRUE;
    }

    @Override
    public List<CollectorResponse> queryCollectorInfo(CollectorQueryRequest request) {
        List<CollectorResponse> collectorResponses = new ArrayList<>();

        // 查询接口优先取redis中数据,如查询不到再去查数据库
        // 先获得redis中的数据，转为对象列表
        List<CollectorUploadEntity> list = new ArrayList<>();
        List<Object> latestData = cacheClient.getLatestData(RedisConstants.REDIS_KEY_PREFIX);
        latestData.forEach(obj -> {
            CollectorUploadEntity bean = JSONUtil.toBean(String.valueOf(obj), CollectorUploadEntity.class);
            list.add(bean);
        });
        // 筛选符合条件的数据 filter（），返回新数组，常与steam一起使用，注意结尾要结束流即.collect(Collectors.toList())
        List<CollectorUploadEntity> collect = list.stream().filter(q ->
                Objects.equals(q.getEndpoint(), request.getEndpoint()) && Objects.equals(q.getMetric(), request.getMetric()) &&
                q.getTimestamp() >= request.getStart_ts() && q.getTimestamp() <= request.getEnd_ts()).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            getCollectorResponse(collect, collectorResponses);
            return collectorResponses;
        }

        // 在数据库查，LambdaQueryWrapper是MyBatis-Plus提供的一种查询构建器，相当于sql语句
        // 通过Lambda表达式来构建查询条件，使用起来比传统的SQL语句更加简洁和易读
        LambdaQueryWrapper<CollectorUploadEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CollectorUploadEntity::getEndpoint, request.getEndpoint())
                .eq(CollectorUploadEntity::getMetric, request.getMetric())
                .between(CollectorUploadEntity::getTimestamp, request.getStart_ts(), request.getEnd_ts());
        List<CollectorUploadEntity> collectorUploadEntities = collectorMapper.selectList(lambdaQueryWrapper);

        getCollectorResponse(collectorUploadEntities, collectorResponses);
        return collectorResponses;
    }

    // 根据符合条件的数据库信息，返回需要的信息对象
    private static void getCollectorResponse(List<CollectorUploadEntity> collectorUploadEntities, List<CollectorResponse> collectorResponses) {
        // 分组查询
        // 依据是每个实体的metric和endpoint拼接成的字符串，利用Collectors.groupingBy方法生成一个Map，其中键是分组标识，值是属于该组的实体列表
        Map<String, List<CollectorUploadEntity>> map = collectorUploadEntities.stream()
                .collect(Collectors.groupingBy(o -> o.getMetric() + "-" + o.getEndpoint(), Collectors.toList()));

        // 遍历map的键，获取每个分组的实体列表，并构建CollectorResponse对象
        map.keySet().forEach(q -> {
            CollectorResponse response = new CollectorResponse();

            // 每个分组的metric值是一样的，取第一个即可
            List<CollectorUploadEntity> mapList = map.get(q);
            response.setMetric(mapList.get(0).getMetric());

            List<CollectorResponse.Values> values = new ArrayList<>();
            mapList.forEach(tt -> {
                CollectorResponse.Values value = new CollectorResponse.Values();
                value.setValue(tt.getValue());
                value.setTimestamp(tt.getTimestamp());
                values.add(value);
            });
            response.setValues(values);

            collectorResponses.add(response);
        });
    }
}
