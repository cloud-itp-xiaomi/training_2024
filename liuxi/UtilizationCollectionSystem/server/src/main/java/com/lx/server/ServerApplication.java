package com.lx.server;

import com.lx.server.dao.RedisDao;
import com.lx.server.pojo.Result;
import com.lx.server.pojo.Utilization;
import com.lx.server.service.UtilizationService;
import com.lx.server.utils.GetBeanUtil;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootApplication
@MapperScan("com.lx.server.mapper")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
