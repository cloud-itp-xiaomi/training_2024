package com.example.xiaomi1.controller;

import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.entity.MetricData;
import com.example.xiaomi1.service.MetricService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServerController.class)
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricService metricService;

    @Test
    void receiveMetric() throws Exception {
        Mockito.doNothing().when(metricService).saveMetric(any(Metric.class));

        // 保存并断言返回结果
        mockMvc.perform(MockMvcRequestBuilders.post("/api/metric/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metric\":\"cpu.used.percent\",\"endpoint\":\"test-computer\"," +
                                "\"step\":60,\"timestamp\":1000000000,\"value\":10.22}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":200,\"message\":\"ok\",\"data\": \"\"}"));
    }

    @Test
    void queryMetrics() throws Exception {
        Metric metric = new Metric();
        metric.setMetric("cpu.used.percent");
        metric.setEndpoint("test-computer");
        metric.setStep(60);
        metric.setTimestamp(1000000000);
        metric.setValue(10.22);

        // 先存
        Mockito.doNothing().when(metricService).saveMetric(metric);

        // 保存数据
        mockMvc.perform(MockMvcRequestBuilders.post("/api/metric/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"metric\":\"cpu.used.percent\",\"endpoint\":\"test-computer\"," +
                                "\"step\":60,\"timestamp\":1000000000,\"value\":10.22}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":200,\"message\":\"ok\",\"data\":\"\"}"));

        // 后查
        MetricData metricData = new MetricData("cpu.used.percent",1000000000,10.22);

        Mockito.when(metricService.getMetrics(anyString(),anyString(),anyLong(),anyLong()))
                        .thenReturn(Collections.singletonList(metricData));
        // 查询并断言返回结果
        mockMvc.perform(MockMvcRequestBuilders.get("/api/metric/query")
                        .param("endpoint", "test-computer")
                        .param("metric", "cpu.used.percent")
                        .param("start_ts", "999999999")
                        .param("end_ts", "1000000001"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":200,\"message\":\"ok\",\"data\":" +
                        "[{\"metric\":\"cpu.used.percent\",\"values\":[{\"timestamp\":1000000000,\"value\":10.22}]}]}"));

    }
}
