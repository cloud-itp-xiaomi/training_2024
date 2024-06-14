package com.example.demo01.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
//用于指示该类是一个配置类。配置类主要用于替代传统的XML配置文件，以更加面向对象的方式进行Spring应用上下文的配置。
// 在配置类中，可以使用如@Bean等注解来声明和定义Bean，这些Bean将被Spring容器管理，用于依赖注入等目的。
public class ScheduleConfig implements SchedulingConfigurer {

    //为Spring框架中的定时任务注册器(ScheduledTaskRegistrar)配置自定义的任务调度器(TaskScheduler)
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //通过getTaskScheduler()方法获取一个TaskScheduler实例，并将其设置到ScheduledTaskRegistrar中，用于执行定时任务
        taskRegistrar.setTaskScheduler(getTaskScheduler());
    }

    @Bean
    public TaskScheduler getTaskScheduler() {
        //创建了一个ThreadPoolTaskScheduler实例，线程池任务调度器，设置了线程池大小为3，线程名前缀为"worker-"，并设置了在关闭时等待任务完成。
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.setThreadNamePrefix("worker-");
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return taskScheduler;
    }

}
