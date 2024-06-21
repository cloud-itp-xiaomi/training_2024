package com.lx.server.controller;

import com.lx.server.pojo.Result;
import com.lx.server.service.UtilizationService;
import com.lx.server.utils.GetBeanUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/metric")
@DependsOn(value = "getBeanUtil")
@CrossOrigin
public class UtilizationController {

    UtilizationService service = GetBeanUtil.getBean(UtilizationService.class);

    //查询全部指标下的数据
    @GetMapping("query")
    public Result query(@RequestParam("endpoint") String endpoint,
                        @RequestParam("start_ts") Long start_ts,
                        @RequestParam("end_ts") Long end_ts) {
        return service.query(endpoint , start_ts , end_ts);
    }

    //根据指标查询
    @GetMapping("query/by-metric")
    public Result query(@RequestParam("endpoint") String endpoint,
                        @RequestParam("metric") String metric,
                        @RequestParam("start_ts") Long start_ts,
                        @RequestParam("end_ts") Long end_ts) {
        return service.queryByMetric(endpoint, metric, start_ts, end_ts);
    }
}
