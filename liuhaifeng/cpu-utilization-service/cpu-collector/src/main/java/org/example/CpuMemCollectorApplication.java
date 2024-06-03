package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.CpuMemClient;
import org.example.fegin.config.DefaultFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:26
 */
@Slf4j
@SpringBootApplication
@EnableFeignClients(clients = {CpuMemClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class CpuMemCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CpuMemCollectorApplication.class, args);
    }
}
