package com.example.hostcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HostCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostCollectorApplication.class, args);
    }

}
