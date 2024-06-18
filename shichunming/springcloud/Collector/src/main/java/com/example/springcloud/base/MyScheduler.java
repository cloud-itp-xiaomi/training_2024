package com.example.springcloud.base;


import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MyScheduler
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 15:52
 **/
@Configuration
public class MyScheduler {
    @Autowired
    private CollectMsgJob collectMsgJob;
    @Value("${schedule.quartzTime}")
    private Integer interval;
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(collectMsgJob.getClass())
                .withIdentity("myJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(interval) // 每10秒执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("myTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
    /**
     *  TODO 热更新待完成
     */
    @Bean
    public Trigger updateTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(interval) // 每10秒执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("myTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
