package com.example.mi1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.mi1")
public class Mi1Application {
    public static void main(String[] args){
        SpringApplication.run(Mi1Application.class, args);
    }
}
