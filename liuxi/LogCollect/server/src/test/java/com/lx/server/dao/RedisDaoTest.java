package com.lx.server.dao;

import com.lx.server.pojo.Utilization;
import com.lx.server.service.MessageConsumeService;
import com.lx.server.utils.GetBeanUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@DependsOn(value = "getBeanUtil")
class RedisDaoTest {

    @Autowired
    private GetBeanUtil getBeanUtil;

    private RedisDao redisDao;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> operations ;

    /**
     * 初始化属性
     */
    @PostConstruct
    public void testGetBeanUtil() {
        redisDao = getBeanUtil.getBean(RedisDao.class);
    }


    @Test
    void addRedis() {
        Utilization utilization = new Utilization();
        utilization.setId(2L);
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        Set<String> keys = operations.getOperations().keys("*");
        int size1 = keys.size();
        redisDao.addRedis(utilization);
        keys = operations.getOperations().keys("*");
        int size2 = keys.size();
        assert size1 + 1  == size2;
    }

    @Test
    void queryByMetricRedis() {
        List<Utilization> result = redisDao.queryByMetricRedis("192.168.138.134", "cpu.used.percent",604071631L, 604071631L);
        List<Utilization> testRes = new ArrayList<>();
        Utilization utilization = new Utilization(75L,"cpu.used.percent", "192.168.138.134", 604071631L, 60L , 0.6);
        testRes.add(utilization);
        boolean res = equals(result.get(0), testRes.get(0));
        assert res == true;

    }

    boolean equals(Utilization u1 , Utilization u2) {
        boolean res = false;
        if(u1.getId() == u2.getId()
                && u1.getEndpoint().equals(u2.getEndpoint())
                && u1.getMetric().equals(u2.getMetric())
                && u1.getTimestamp().equals(u2.getTimestamp())) {
            res = true;
        }
        return res;
    }

    @Test
    void queryByRedis() {


        List<Utilization> result = redisDao.queryByRedis("192.168.138.134", 604071631L, 604071631L);
        int size = result.size();
        assert 1 == size;
    }

    @Test
    void delete() {
        Set<String> keys = operations.getOperations().keys("*");
        int size1 = keys.size();
        if(keys != null && keys.size() != 0)
            redisDao.delete(keys);
        keys = operations.getOperations().keys("*");
        int size2 = keys.size();
        assert size1 == size2 + 1;
    }

    @Test
    void buildStr() {
        String str = redisDao.buildStr("mem.used.percent", "6853817426de" , "611062457");
        String testStr = "mem.used.percent_6853817426de_611062457";
        assert testStr.equals(str);
    }
}