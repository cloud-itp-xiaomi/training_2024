package com.txh.xiaomi2024.work.collector.service.impl;

import com.txh.xiaomi2024.work.collector.common.CommonResult;
import com.txh.xiaomi2024.work.collector.common.JobType;
import com.txh.xiaomi2024.work.collector.common.ResultCode;
import com.txh.xiaomi2024.work.collector.job.CollectLogJob;
import com.txh.xiaomi2024.work.collector.job.CollectUtilizationJob;
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

    @Override
    public CommonResult<?> startJob(String jobName,
                                    String jobGroup,
                                    String triggerName,
                                    String triggerGroup,
                                    Scheduler scheduler) {
        // 1.定义jobDetail
        JobDetail jobDetail = null;
        if (jobName.equals(JobType.LOG.getJobDetail())) {
            jobDetail = JobBuilder.newJob(CollectLogJob.class)
                    .withIdentity(jobName, jobGroup)
                    .build();
        } else if (jobName.equals(JobType.UTILIZATION.getJobDetail())) {
            jobDetail = JobBuilder.newJob(CollectUtilizationJob.class)
                    .withIdentity(jobName, jobGroup)
                    .build();
        }

        // 返回采纳书校验失败
        if (jobDetail == null) {
            return CommonResult.validateFailed("jobDetail is null");
        }
        // 2、构建Trigger实例,每隔1分钟执行一次
        SimpleScheduleBuilder ssb = SimpleScheduleBuilder.repeatMinutelyForever(1);
        // 3、设置misfire策略
        ssb.withMisfireHandlingInstructionIgnoreMisfires();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                .startNow()//立即生效
                .withSchedule(ssb)//调度
                .build();

        // 4、执行
        try {
            scheduler.scheduleJob(
                    jobDetail,
                    trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            return CommonResult.failed("scheduler error");
        }
        return CommonResult.success("start job success");
    }

    @Override
    public CommonResult<?> pauseJob(String jobName,
                         String jobGroup,
                         Scheduler scheduler) {
        if (!jobName.equals(JobType.LOG.getJobDetail()) && !jobName.equals(JobType.UTILIZATION.getJobDetail())) {
            return CommonResult.validateFailed("jobName not match");
        }
        // 通过JobName以及JobGroup获得JobKey
        JobKey jobKey = JobKey.jobKey(
                jobName,
                jobGroup);
        try {
            scheduler.pauseJob(jobKey);
            return CommonResult.success("pause job success");
        } catch (SchedulerException e) {
            return CommonResult.failed("pause job error");
        }
    }

    @Override
    public CommonResult<?> resumeJob(String jobName,
                          String jobGroup,
                          Scheduler scheduler) {
        if (!jobName.equals(JobType.LOG.getJobDetail()) && !jobName.equals(JobType.UTILIZATION.getJobDetail())) {
            return CommonResult.validateFailed("jobName not match");
        }
        // 通过JobName以及JobGroup获得JobKey
        JobKey jobKey = JobKey.jobKey(
                jobName,
                jobGroup);
        try {
            scheduler.resumeJob(jobKey);
            return CommonResult.success("resume job success");
        } catch (SchedulerException e) {
            return CommonResult.failed("resume job error");
        }
    }

    @Override
    public CommonResult<?> deleteJob(String jobName,
                          String jobGroup,
                          Scheduler scheduler) {
        if (!jobName.equals(JobType.LOG.getJobDetail()) && !jobName.equals(JobType.UTILIZATION.getJobDetail())) {
            return CommonResult.validateFailed("jobName not match");
        }
            // 通过JobName以及JobGroup获得JobKey
        JobKey jobKey = JobKey.jobKey(
                jobName,
                jobGroup);
        try {
            scheduler.deleteJob(jobKey);
            return CommonResult.success("delete job success");
        } catch (SchedulerException e) {
            return CommonResult.failed("delete job error");
        }
    }
}
