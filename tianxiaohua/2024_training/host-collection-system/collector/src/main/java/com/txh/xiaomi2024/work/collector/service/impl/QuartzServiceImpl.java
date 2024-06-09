package com.txh.xiaomi2024.work.collector.service.impl;

import com.txh.xiaomi2024.work.collector.job.UtilizationCollectJob;
import com.txh.xiaomi2024.work.collector.service.QuartzService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author txh
 * 定义Quartz定时任务触发
 */
@Service
public class QuartzServiceImpl implements QuartzService {
    private final Scheduler scheduler;

    @Autowired
    public QuartzServiceImpl() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        this.scheduler = schedulerFactory.getScheduler();
    }

    @Override
    public void startJob(String jobName, String jobGroup) {
        // 1.定义jobDetail
        JobDetail jobDetail = JobBuilder.newJob(UtilizationCollectJob.class)
                .usingJobData("jobDetail_collector", "获取主机cpu和内存利用率")
                .withIdentity(jobName, jobGroup)
                .build();
        // 2、构建Trigger实例,每隔1分钟执行一次
        SimpleScheduleBuilder ssb = SimpleScheduleBuilder.repeatMinutelyForever(1);
        // 3、设置misfire策略
        ssb.withMisfireHandlingInstructionIgnoreMisfires();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_work", "trigger_group")
                .usingJobData("trigger_work", "这是jobDetail_collector的trigger")
                .startNow()//立即生效
                .withSchedule(ssb)//调度
                .build();

        // 4、执行
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
