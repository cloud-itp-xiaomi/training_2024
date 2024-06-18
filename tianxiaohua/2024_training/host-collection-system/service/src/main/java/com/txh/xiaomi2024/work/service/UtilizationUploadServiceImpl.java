package com.txh.xiaomi2024.work.service;

import com.txh.xiaomi2024.work.UtilizationUploadService;
import com.txh.xiaomi2024.work.service.bean.Utilization;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.dao.UtilizationMysqlMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author txh
 * 上报数据
 */
@DubboService// 通过这个配置可以基于 Spring Boot 去发布 Dubbo 服务
@Service
public class UtilizationUploadServiceImpl implements UtilizationUploadService {
    private final UtilizationMysqlMapper utilizationMysqlMapper;
    private final RedisDao redisDao;


    public UtilizationUploadServiceImpl(UtilizationMysqlMapper utilizationMysqlMapper, RedisDao redisDao) {
        this.utilizationMysqlMapper = utilizationMysqlMapper;
        this.redisDao = redisDao;
    }

    @Override
    public String upload(String metric, String endpoint, int step, long collect_time, double value_metric) {
        Utilization utilization = new Utilization(metric, endpoint, collect_time, step, value_metric);
        utilizationMysqlMapper.addData(utilization);
        List<Utilization> list = utilizationMysqlMapper.queryByEMS(endpoint, metric, collect_time);
        System.out.println(list.size());
        // 将最新的数据存储到redis
        setLatestInRedis("utilization", list.get(0));
        return "上报数据并完成存储！";
    }

    /**
     * 将最新的10条数据存储到redis
     * @param key 对应存储的key
     * @return
     */
    private void setLatestInRedis(String key, Utilization u) {
        redisDao.lPush(key, u);
        long size = redisDao.lSize(key);
        // 如果redis中存储的数据量大于10，就删除最开始插入的数据
        if (size > 10) {
            redisDao.leftPop(key);
        }
    }
}
