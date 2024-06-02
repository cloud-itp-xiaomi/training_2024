package org.qiaojingjing.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.pojo.dto.MetricDTO;
import org.qiaojingjing.pojo.dto.MetricsDTO;
import org.qiaojingjing.pojo.entity.Metric;
import org.qiaojingjing.pojo.vo.MetricVO;
import org.qiaojingjing.mapper.MetricMapper;
import org.qiaojingjing.service.ComputerService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComputerServiceImpl implements ComputerService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MetricMapper metricMapper;

    /**
     * Redis 作为缓存，
     * 只存储 Server 接收到的 CPU 利用率和内存利用率数据的最近十条数据。
     *
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/
    @Override
    public void upload(List<MetricsDTO> metricsDTO) {
        //这里采用列表数据结构。1.控制数量方便。 2.插入效率高
        //设置 key，使用 metricsDTO.getEndpoint() 作为列表的名称
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        String key = metricsDTO.get(1).getEndpoint();
        for (MetricsDTO dto : metricsDTO) {
            Metric metric = new Metric();
            BeanUtils.copyProperties(dto, metric);
            listOps.leftPush(key, metric);
            listOps.trim(key, 0, 19);
        }


        List<Metric> metrics = new ArrayList<>();
        for (MetricsDTO dto : metricsDTO) {
            Metric metric = new Metric();
            BeanUtils.copyProperties(dto, metric);
            metrics.add(metric);
        }
        System.out.println(metrics);
        // 存入数据库
        metricMapper.save(metrics);
    }

    /**
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/
    @Override
    public List<MetricVO> query(MetricDTO metricDTO) {
        ListOperations listOperations = redisTemplate.opsForList();
        String key = metricDTO.getEndpoint();
        List<MetricVO> list;
        //获取所有数据
        List<Metric> metrics = listOperations.range(key, 0, -1);

        Map<String, List<Metric>> metricsMap = metrics.stream().collect(Collectors.groupingBy(Metric::getMetric));
        Long end = metrics.get(0).getTimestamp();
        Long start = metrics.get(metrics.size()-1).getTimestamp();
        Long startTs = metricDTO.getStartTs();
        Long endTs = metricDTO.getEndTs();
        if (startTs >= start && endTs <= end) {
            log.info("通过redis查询数据...");
            list = retrieveDataFromRedis(startTs, endTs, metricsMap);
            return list;

        }


        list = metricMapper.query(metricDTO);
        return list;
    }

    //从redis从检索数据
    private List<MetricVO> retrieveDataFromRedis(Long startTs, Long endTs, Map<String, List<Metric>> metricsMap) {
        List<MetricVO> list = new ArrayList<>();
        Set<Map.Entry<String, List<Metric>>> entries = metricsMap.entrySet();
        for (Map.Entry<String, List<Metric>> entry : entries) {
            String metricName = entry.getKey();
            List<Metric> metricData = entry.getValue();
            MetricVO metricVO = new MetricVO();
            metricVO.setMetric(metricName);
            List<MetricVO.Value> values = new ArrayList<>();

            for (Metric metric : metricData) {
                if (metric.getTimestamp() >= startTs && metric.getTimestamp() <= endTs) {
                    MetricVO.Value value = new MetricVO.Value();
                    value.setTimestamp(metric.getTimestamp());
                    value.setValue(metric.getValue());
                    values.add(value);
                }
            }
            metricVO.setValues(values);
            list.add(metricVO);
        }

        return list;

    }
}
