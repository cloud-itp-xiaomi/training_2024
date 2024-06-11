package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class LogCollectorApplication {
  public static void main(String[] args) {
    SpringApplication.run(LogCollectorApplication.class, args);
  }
}

