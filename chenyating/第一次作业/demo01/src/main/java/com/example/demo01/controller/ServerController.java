package com.example.demo01.controller;


import com.example.demo01.bean.DataItem;
import com.example.demo01.bean.MetricVO;
import com.example.demo01.bean.R;
import com.example.demo01.mapper.DataMapper;
import com.example.demo01.param.MetricForm;
import com.example.demo01.param.MetricQueryForm;
import com.example.demo01.param.MetricResultForm;
import com.example.demo01.service.CollectService;
import com.example.demo01.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/metric")
@RequiredArgsConstructor
public class ServerController {

    private final DataMapper dataMapper;
    private final CollectService collectService;

    @PostMapping("/upload")
    public R<Boolean> upload(@RequestBody @NotEmpty(message = "提交数据不能为空") List<MetricForm> metricFormList) {
        for (MetricForm metricForm : metricFormList) {
            DataItem sysMetric = new DataItem();
            sysMetric.setMetricName(metricForm.getMetric());
            sysMetric.setEndpoint(metricForm.getEndpoint());
            sysMetric.setTimestamp(TimeUtils.format(metricForm.getTimestamp()));
            sysMetric.setStep(metricForm.getStep());
            sysMetric.setPercentValue(metricForm.getValue());
            sysMetric.setCreateId("api");

            dataMapper.insert(sysMetric);
            collectService.syncRedis(metricForm.getMetric());
        }

        return R.ok(Boolean.TRUE);
    }

    @GetMapping("/query")
    public R<List<MetricResultForm>> upload(@RequestBody @NotNull(message = "请求参数不能为空") MetricQueryForm query) {
        final List<MetricVO> metricVOList = dataMapper.selectByCondition(query.getEndpoint(),
                query.getMetric(), TimeUtils.format(query.getStartTime()), TimeUtils.format(query.getEndTime()));

        final Map<String, List<MetricVO>> metricResultMap =
                metricVOList.stream().collect(Collectors.groupingBy(MetricVO::getMetric));
        final List<MetricResultForm> resultFormList = new ArrayList<>();
        metricResultMap.forEach((metric, metricList) -> {
            final MetricResultForm metricResultForm = new MetricResultForm(metric, metricList);
            resultFormList.add(metricResultForm);
        });
        return R.ok(resultFormList);
    }

}
