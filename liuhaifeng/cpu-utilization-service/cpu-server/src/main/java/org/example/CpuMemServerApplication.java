package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:23
 */
@SpringBootApplication
@EnableTransactionManagement
public class CpuMemServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CpuMemServerApplication.class, args);
    }
}
