package com.cl.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * server模块启动类
 *
 * @author: tressures
 * @date: 2024-05-26 17:06:02
 */
@SpringBootApplication
@ComponentScan("com.cl")
@MapperScan("com.cl.server.mapper")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class);
    }

}
