package com.example.springcloud;


import com.example.springcloud.base.CfgConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @ClassName ServerApplication
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-05 11:16
 **/
//@ComponentScan(basePackages = {"com.example.springcloud.service", "com.example.springcloud.controller", "com.example.springcloud.base"})
@EnableConfigurationProperties(CfgConfig.class)
@SpringBootApplication
@EnableCaching
@MapperScan("com.example.springcloud.mapper")
public class ServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}

