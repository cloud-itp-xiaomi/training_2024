package com.xiaomi.work1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.xiaomi.work1.mapper")
public class Work1Application {

    public static void main(String[] args) {
        SpringApplication.run(Work1Application.class, args);
    }

}
