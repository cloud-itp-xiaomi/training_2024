package com.txh.xiaomi2024.work.service.controller;

import com.txh.xiaomi2024.work.service.bean.Utilization;
import com.txh.xiaomi2024.work.service.common.CommonResult;
import com.txh.xiaomi2024.work.service.service.UtilizationService;
import com.txh.xiaomi2024.work.service.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
                                          @RequestParam(value = "end_ts") long endTs,
                                          @RequestParam(value = "current_page") int currentPage,
                                          @RequestParam(value = "page_size") int pageSize) {
        // 参数校验
        if (StringUtils.isEmpty(metric)
                || StringUtils.isEmpty(endpoint)
                || startTs < 0 || endTs < 0
                || startTs > endTs || currentPage < 0 || pageSize < 0) {
            return CommonResult.validateFailed("参数不完整或有误");
        }

        List<Utilization> utilizationList = utilizationService.getUtilization(
                metric,
                endpoint,
                startTs,
                endTs);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("metric",metric);
        List<Map<String,Object>> values = new ArrayList<>();
        for (Utilization utilization : utilizationList) {
            Map<String,Object> value = new HashMap<>();
            value.put("timestamp",utilization.getCollect_time());
            value.put("value",utilization.getValue_metric());
            values.add(value);
        }

        long pageNum = (long) Math.ceil(((double) values.size() / pageSize));
        long totalNum = values.size();
        objectMap.put("page_num",pageNum);
        objectMap.put("total_num",totalNum);

        if (values.isEmpty()) {
            objectMap.put("values",null);
        } else {
            int fromIndex = (currentPage - 1) * pageSize;
            int toIndex = Math.min(values.size(), fromIndex + pageSize);
            objectMap.put("values",values.subList(fromIndex, toIndex));
        }
        return CommonResult.success(
                objectMap,
                "ok");
    }
}
