package com.lx.server.dao;

import com.lx.server.pojo.Result;
import com.lx.server.pojo.Utilization;
import com.lx.server.utils.GetBeanUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> operations ;

    @Test
    void addRedis() {
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        redisDao.addRedis(utilization);
    }

    @Test
    void queryByMetricRedis() {
        List<Utilization> result = redisDao.queryByMetricRedis("6853817426de", "cpu.used.percent",611062457L, 611062557L);
        for(Utilization utilization : result) {
            System.out.println(utilization);
        }
    }

    @Test
    void queryByRedis() {

        List<Utilization> result = redisDao.queryByRedis("6853817426de", 611062457L, 611062557L);
        for(Utilization utilization : result) {
            System.out.println(utilization);
        }
    }

    @Test
    void delete() {
        Set<String> keys = operations.getOperations().keys("*");
        if(keys != null && keys.size() != 0)
            redisDao.delete(keys);
    }

    @Test
    void buildStr() {
        String str = redisDao.buildStr("mem.used.percent", "6853817426de" , "611062457");
        System.out.println(str);
    }
}