package com.winter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 返回一个线程池
 * 后期每个线程都用于监听配置的配置文件
 * */
@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor executor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);  //设置和兴线程数
        taskExecutor.setMaxPoolSize(10);  //设置最大线程数
        taskExecutor.setQueueCapacity(100);  //设置任务队列能够承载的最大任务数目
        taskExecutor.setThreadNamePrefix("LogWatcher-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
