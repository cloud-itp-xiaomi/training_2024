package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:23
 */
@Slf4j
@EnableCaching// 开启缓存注解
@SpringBootApplication
@EnableTransactionManagement// 开启事务注解
public class UtilizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilizationServerApplication.class, args);
        log.info("utilization-server 启动成功");
    }
}
