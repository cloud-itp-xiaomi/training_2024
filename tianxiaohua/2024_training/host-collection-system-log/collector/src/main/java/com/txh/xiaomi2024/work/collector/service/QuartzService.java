package com.txh.xiaomi2024.work.collector.service;

import com.txh.xiaomi2024.work.collector.common.CommonResult;
import com.txh.xiaomi2024.work.collector.common.ResultCode;
import org.quartz.Scheduler;

public interface QuartzService {
    CommonResult<?> startJob(String jobName, String jobGroup, String triggerName, String triggerGroup, Scheduler scheduler);
    CommonResult<?> pauseJob(String jobName, String jobGroup, Scheduler scheduler);
    CommonResult<?> resumeJob(String jobName, String jobGroup, Scheduler scheduler);
    CommonResult<?> deleteJob(String jobName, String jobGroup, Scheduler scheduler);
}
