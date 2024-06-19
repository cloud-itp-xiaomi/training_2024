package com.txh.xiaomi2024.work.service;

import com.txh.xiaomi2024.work.UtilizationUploadService;
import com.txh.xiaomi2024.work.service.bean.Utilization;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.dao.UtilizationMysqlMapper;
import io.lettuce.core.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @author txh
 * 上报数据
 */
@DubboService
@Service
@Slf4j
public class UtilizationUploadServiceImpl implements UtilizationUploadService {
    private final UtilizationMysqlMapper utilizationMysqlMapper;
    private final RedisDao redisDao;


    public UtilizationUploadServiceImpl(UtilizationMysqlMapper utilizationMysqlMapper,
                                        RedisDao redisDao) {
        this.utilizationMysqlMapper = utilizationMysqlMapper;
        this.redisDao = redisDao;
    }

    @Override
    public String uploadUtilization(String metric,
                                    String endpoint,
                                    int step,
                                    long collect_time,
                                    double value_metric) {
        // 参数校验
        if (StringUtils.isEmpty(metric)
                || StringUtils.isEmpty(endpoint)
                || !metric.equals("cpu.used.percent") && !metric.equals("mem.used.percent")
                || step <= 0 || collect_time < 0 || value_metric < 0 || value_metric > 100) {
            throw new IllegalArgumentException("参数校验失败：参数不能为空且 step、collect_time必须大于0、value_metric 必须大于0小于100");
        }

        Utilization utilization = new Utilization(
                metric,
                endpoint,
                collect_time,
                step,
                value_metric);
        try {
            utilizationMysqlMapper.insertData(utilization);
        } catch (MyBatisSystemException e) {
            throw new MyBatisSystemException(e);
        }

        // 将最新的数据存储到redis
        setLatestInRedis(
                "utilization",
                utilization);

        return "上报数据并完成存储！";
    }

    /**
     * 将最新的10条数据存储到redis
     * @param key 对应存储的key
     * @return
     */
    private void setLatestInRedis(String key,
                                  Utilization u) {
        try {
            redisDao.lPush(key, u);
            redisDao.trim(key, -10,-1);
        } catch (RedisCommandTimeoutException | RedisConnectionException | RedisCommandExecutionException |
                 RedisCommandInterruptedException e) {
            throw new RedisException(e);
        }
    }
}
