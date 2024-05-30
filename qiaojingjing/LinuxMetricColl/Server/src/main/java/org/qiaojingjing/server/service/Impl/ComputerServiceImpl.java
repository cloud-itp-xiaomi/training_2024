package org.qiaojingjing.server.service.Impl;

import org.qiaojingjing.server.mapper.MetricMapper;
import org.qiaojingjing.server.pojo.dto.MetricDTO;
import org.qiaojingjing.server.pojo.dto.MetricsDTO;
import org.qiaojingjing.server.pojo.entity.Metric;
import org.qiaojingjing.server.pojo.vo.MetricVO;
import org.qiaojingjing.server.service.ComputerService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerServiceImpl implements ComputerService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MetricMapper metricMapper;

    /**
     * Redis 作为缓存，
     * 只存储 Server 接收到的 CPU 利用率和内存利用率数据的最近十条数据。
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/
    @Override
    public void upload(List<MetricsDTO> metricsDTO) {
        //这里采用列表数据结构。1.控制数量方便。 2.插入效率高
        //设置 key，使用 metricsDTO.getEndpoint() 作为列表的名称
        for (MetricsDTO dto : metricsDTO) {
            String key = dto.getEndpoint();
            ListOperations<String, Object> listOps = redisTemplate.opsForList();
            // 将值添加到列表中
            listOps.leftPush(key, dto);
            // 截断列表，保留最新的 10 个值
            listOps.trim(key, 0, 9);
        }

        List<Metric> metrics = new ArrayList<>();
        Metric metric = new Metric();
        for (MetricsDTO dto : metricsDTO) {
            BeanUtils.copyProperties(dto,metric);
            metrics.add(metric);
        }
        System.out.println(metrics);
        // 存入数据库
        metricMapper.save(metrics);
    }

    /**
     *
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/
    @Override
    public List<MetricVO> query(MetricDTO metricDTO) {
        //TODO 从redis中取出第一条和第十条数据，判断是否在传入的时间戳内
        List<MetricVO> list =  metricMapper.query(metricDTO);
        return list;
    }
}
