package com.winter.config;

import com.winter.service.CpuAndMemoryPerformance;
import com.winter.service.StorageMonitor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用quartz定时调度框架完成定时调度的策略，spring自带的定时任务不适合分布式的应用，
 * 切不支持定时时间的修改
 *
 * 这是定时任务的配置类
 * */

@Configuration
public class QuartzConfig {

    /**
     * 声名定时上传主机cpu和内存数据信息的定时任务
     * */
    @Bean
    public JobDetail jobDetail_uploadCpuMem(){
        return JobBuilder.newJob(CpuAndMemoryPerformance.class)
                .withIdentity("CpuAndMemoryPerformance", "XiaoMi")
                .storeDurably()
                .build();
    }

    /**
     * 声名jobDetail_uploadCpuMem的触发器，设置执行的时机
     * */
    @Bean
    public Trigger trigger_uploadCpuMem(){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail_uploadCpuMem())
                .withIdentity("uploadCpuMem", "trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("*/59 * * * * ?"))
                .build();
    }


    /**
     * 定时上传配置文件中配置的存储方式
     * */
    @Bean
    public JobDetail jobDetail_uploadLogStorage(){
        return JobBuilder.newJob(StorageMonitor.class)
                .withIdentity("LogStorage", "XiaoMi")
                .storeDurably()
                .build();
    }

    /**
     * 声名jobDetail_uploadLogStorage的触发器，设置执行的时机为每30s上传一次
     * */
    @Bean
    public Trigger trigger_uploadLogStorage(){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail_uploadLogStorage())
                .withIdentity("uploadLogStorage", "trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("*/29 * * * * ?"))
                .build();
    }
}
