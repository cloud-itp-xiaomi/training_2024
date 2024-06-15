package org.example;

import lombok.var;
import org.example.log.ConfigReader;
import org.example.rest.DataSender;
import org.example.utilization.Utilization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.Timer;

@SpringBootApplication
@EnableScheduling
public class Main implements CommandLineRunner {
    @Autowired
    private Utilization utilization;
    @Autowired
    private DataSender dataSender;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            final var period = Duration.ofSeconds(60);
            ConfigReader cf = new ConfigReader(dataSender);
            cf.beginTask();
            new Timer().scheduleAtFixedRate(utilization, 0, period.toMillis());
        } catch (Exception e) {
            System.out.println("Error scheduling Utilization task: " + e.getMessage());
        }
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
