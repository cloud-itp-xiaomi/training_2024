package com.txh.xiaomi2024.work.collector.controller;

import com.txh.xiaomi2024.work.collector.common.CommonResult;
import com.txh.xiaomi2024.work.collector.service.QuartzService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author txh
 * 上报控制器
 */
@RestController
@RequestMapping(value = "/api")
public class CollectorController {
    private final QuartzService quartzService;
    private final Scheduler scheduler;
    private int utilizationStatus = 0;
    private int logStatus = 0;

    @Autowired
    public CollectorController(QuartzService quartzService) throws SchedulerException {
        this.quartzService = quartzService;
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        this.scheduler = schedulerFactory.getScheduler();
    }

    @PostMapping("/metric/upload")
    public CommonResult<?> startCollect() {
        utilizationStatus = 1;
        return quartzService.startJob("collector_work1",
                "collectors",
                "trigger_work1",
                "trigger_group",
                scheduler);
    }

    @PostMapping("/metric/pause")
    public CommonResult<?> pauseCollect() {
        utilizationStatus = 2;
        return quartzService.pauseJob("collector_work1",
                "collectors",
                scheduler);
    }

    @PostMapping("/metric/resume")
    public CommonResult<?> resumeCollect() {
        utilizationStatus = 1;
        return quartzService.resumeJob("collector_work1",
                "collectors",
                scheduler);
    }

    @GetMapping( "/metric/get_status")
    public CommonResult<?> getFetchStatus() {
        return CommonResult.success(utilizationStatus);
    }

    @PostMapping("/log/upload")
    public CommonResult<?> startCollectLog() {
        logStatus = 1;
        return quartzService.startJob("collector_work2",
                "collectors",
                "trigger_work2",
                "trigger_group",
                scheduler);
    }

    @PostMapping("/log/pause")
    public CommonResult<?> pauseCollectLog() {
        logStatus = 2;
        return quartzService.pauseJob("collector_work2",
                "collectors",
                scheduler);
    }

    @PostMapping("/log/resume")
    public CommonResult<?> resumeCollectLog() {
        logStatus = 1;
        return quartzService.resumeJob("collector_work2",
                "collectors",
                scheduler);
    }

    @GetMapping( "/log/get_status")
    public CommonResult<?> getFetchStatusLog() {
        return CommonResult.success(logStatus);
    }
}
