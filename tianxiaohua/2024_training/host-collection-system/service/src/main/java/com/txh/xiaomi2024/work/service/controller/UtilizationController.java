package com.txh.xiaomi2024.work.service.controller;

import com.txh.xiaomi2024.work.service.bean.Utilization;
import com.txh.xiaomi2024.work.service.common.CommonResult;
import com.txh.xiaomi2024.work.service.service.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author txh
 * 查询接口
 */
@RestController
@RequestMapping(value = "/api/metric")
public class UtilizationController {
    private final UtilizationService utilizationService;

    @Autowired
    public UtilizationController(UtilizationService utilizationService) {
        this.utilizationService = utilizationService;
    }

    @GetMapping("/query")
    public CommonResult<?> getUtilization(@RequestParam(value = "metric") String metric,
                                          @RequestParam(value = "endpoint") String endpoint,
                                          @RequestParam(value = "start_ts") long startTs,
                                          @RequestParam(value = "end_ts") long endTs) {
        List<Utilization> utilizationList = utilizationService.getUtilization(
                metric,
                endpoint,
                startTs,
                endTs);
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("metric",metric);
        List<Map<String,Object>> values = new ArrayList<>();
        for (Utilization utilization : utilizationList) {
            Map<String,Object> value = new HashMap<>();
            value.put("timestamp",utilization.getCollect_time());
            value.put("value",utilization.getValue_metric());
            values.add(value);
        }
        resultMap.put("values",values);
        list.add(resultMap);
        return CommonResult.success(resultMap, "ok");
    }
}
