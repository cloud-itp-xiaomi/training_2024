package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.LogClient;
import org.example.fegin.clients.UtilizationClient;
import org.example.fegin.config.DefaultFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:26
 */
@Slf4j
@SpringBootApplication
@EnableScheduling // 开启注解方式的任务调度
@EnableFeignClients(clients = {UtilizationClient.class, LogClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class UtilizationCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilizationCollectorApplication.class, args);
        log.info("utilization-collector 启动成功");
    }
}
