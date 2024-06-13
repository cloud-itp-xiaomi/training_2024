package com.lx.server;

import com.lx.server.dao.RedisDao;
import com.lx.server.pojo.Utilization;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.lx.server.mapper")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        RedisDao redisDao = new RedisDao();
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        redisDao.addRedis(utilization);
    }
}
