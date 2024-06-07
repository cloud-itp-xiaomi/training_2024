package com.cl.collector;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author: tressures
 * @date: 2024-05-26 17:06:02
 */
@SpringBootApplication
@ComponentScan("com.cl")
public class CollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class);
    }

}
