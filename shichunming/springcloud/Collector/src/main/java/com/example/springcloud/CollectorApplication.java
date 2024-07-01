package com.example.springcloud;

import com.example.springcloud.base.CfgConfig;
import com.example.springcloud.base.FileUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @ClassName CollectorApplication
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 00:33
 **/
@SpringBootApplication
@EnableConfigurationProperties(CfgConfig.class)
@MapperScan("com.example.springcloud.mapper")
@EnableScheduling //开启定时任务
public class CollectorApplication {


    public static void main(String[] args) throws Exception {
        //SpringApplication.run(CollectorApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(CollectorApplication.class, args);
        FileUtil fileUtil = context.getBean(FileUtil.class);
        fileUtil.execute();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
