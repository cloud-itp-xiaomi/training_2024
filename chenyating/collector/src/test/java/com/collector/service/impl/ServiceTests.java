package com.collector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.collector.bean.entity.MetircUploadEntity;
import com.collector.bean.enums.MetricEnum;
import com.collector.bean.request.MetircQueryRequest;
import com.collector.bean.request.MetircUploadRequest;
import com.collector.bean.response.MetricResponse;
import com.collector.mapper.MetricMapper;
import com.collector.service.IMetricService;
import com.collector.utils.Common;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ServiceTests {
    // 测试采集利用率的服务类
    @Resource
    private IMetricService iMetricService;
    @Resource
    private MetricMapper collectorUploadMapper;
    @Resource
    private Common common = new Common(); // 不初始化，会为空报错
    private List<MetircUploadRequest> requests1 = new ArrayList<>();
    long timeMillis = System.currentTimeMillis();
    int timestamp = Integer.valueOf(String.valueOf(timeMillis/1000));
    String hostName = common.getHostName();

    // 插入一个采集数据，输出结果，并输出全部数据库数据，检查是否真的插入
    @Test
    @Transactional
    @Rollback(true)
    void testUpload() {
        MetircUploadRequest request1 = new MetircUploadRequest();
        request1.setStep(60);
        request1.setValue(88.8);
        request1.setEndpoint(hostName);
        request1.setTimestamp(timestamp);
        request1.setMetric(MetricEnum.CPU.getMessage());
        requests1.add(request1);

        iMetricService.upload(requests1);
        System.out.println("操作成功！");

        LambdaQueryWrapper<MetircUploadEntity> wrapper = new LambdaQueryWrapper<>();
        List<MetircUploadEntity> allEntities = collectorUploadMapper.selectList(wrapper);
        allEntities.forEach(System.out::println);
    }

    @Test
    void testQuery() {
        MetircQueryRequest request2 = new MetircQueryRequest();
        request2.setEndpoint("LAPTOP-L6IJ633H");
        request2.setMetric(MetricEnum.CPU.getMessage());
        request2.setStart_ts(1718864100);
        request2.setEnd_ts(1718864666);

        List<MetricResponse> metricRespons = new ArrayList<>();
        metricRespons = iMetricService.queryMetricInfo(request2);

        System.out.println(metricRespons);
    }

}