package com.txh.xiaomi2024.work.collector.controller;

import com.txh.xiaomi2024.work.collector.common.CommonResult;
import com.txh.xiaomi2024.work.collector.job.CollectJob;
import com.txh.xiaomi2024.work.collector.service.QuartzService;
import com.txh.xiaomi2024.work.collector.service.UtilizationCollectService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CollectorController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    @PostMapping("/metric/upload")
    public CommonResult<?> startCollect() {
        quartzService.startJob("collector",
                "collectors");
        return CommonResult.success("上报数据成功");
    }


}
