package com.example.springcloud.service;

import com.example.springcloud.base.BaseJsonUtils;
import com.example.springcloud.base.Response;
import com.example.springcloud.base.SnowflakeIdGenerator;
import com.example.springcloud.controller.ServerController;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.response.MetricQueryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName serviceTest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-05 15:35
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private ServerController serverController;

    @Test
    public void insertTest() {
        for (int i = 0; i < 1; i++) {
            MetricUploadRequest request = new MetricUploadRequest("cpu.used.percent", "my-computer1", 1717596227L, 60, 60.10f);
            collectorService.metricUpload(request);
            Response<Void> response = serverController.metricUpload(request);
            System.out.println(BaseJsonUtils.writeValue(response));
        }
    }

    @Test
    public void queryTest() {
        MetricQueryRequest request = new MetricQueryRequest();
        request.setEndpoint("my-computer");
//        request.setMetric("cpu.used.percent");
        request.setStart_ts(1717596227L);
        request.setEnd_ts(1717598947L);
        Response<List<MetricQueryResponse>> response = serverController.metricQuery(request);
        System.out.println(BaseJsonUtils.writeValue(response));
        response.getData().stream().forEach(System.out::println);
        System.out.println(collectorService.queryMetric(request));
    }

    @Test
    public void idTest() {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1L, 2L);
        System.out.println(snowflakeIdGenerator.nextId());
    }
}
