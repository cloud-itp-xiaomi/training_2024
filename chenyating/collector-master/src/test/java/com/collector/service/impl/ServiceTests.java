package com.collector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.collector.bean.entity.CollectorUploadEntity;
import com.collector.bean.enums.MetricEnum;
import com.collector.bean.request.CollectorQueryRequest;
import com.collector.bean.request.CollectorUploadRequest;
import com.collector.bean.response.CollectorResponse;
import com.collector.mapper.CollectorMapper;
import com.collector.service.ICollectorService;
import com.collector.utils.Common;
import com.collector.utils.MyResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {
    @Resource
    private ICollectorService iCollectorService;
    @Resource
    private  CollectorMapper collectorUploadMapper;
    @Resource
    private Common common = new Common(); // 不初始化，会为空报错
    private List<CollectorUploadRequest> requests1 = new ArrayList<>();
    long timeMillis = System.currentTimeMillis();
    int timestamp = Integer.valueOf(String.valueOf(timeMillis/1000));
    String hostName = common.getHostName();

    // 插入一个采集数据，输出结果，并输出全部数据库数据，检查是否真的插入
    @Test
    @Transactional
    @Rollback(true)
    void testUpload() {
        CollectorUploadRequest request1 = new CollectorUploadRequest();
        request1.setStep(60);
        request1.setValue(88.8);
        request1.setEndpoint(hostName);
        request1.setTimestamp(timestamp);
        request1.setMetric(MetricEnum.CPU.getMessage());
        requests1.add(request1);

        Boolean result = iCollectorService.upload(requests1);
        System.out.println(result);

        LambdaQueryWrapper<CollectorUploadEntity> wrapper = new LambdaQueryWrapper<>();
        List<CollectorUploadEntity> allEntities = collectorUploadMapper.selectList(wrapper);
        allEntities.forEach(System.out::println);
    }

    @Test
    @Transactional
    @Rollback(true)
    void testQuery() {
        CollectorQueryRequest request2 = new CollectorQueryRequest();
        request2.setEndpoint("LAPTOP-L6IJ633H");
        request2.setMetric(MetricEnum.CPU.getMessage());
        request2.setStart_ts(1718864100);
        request2.setEnd_ts(1718864666);

        List<CollectorResponse> collectorResponses = new ArrayList<>();
        try{
            collectorResponses = iCollectorService.queryCollectorInfo(request2);
            System.out.println(collectorResponses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
