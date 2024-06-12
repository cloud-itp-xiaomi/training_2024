package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.pojo.Utilization;
import com.lx.server.utils.GetBeanUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "HOST_UTILIZATION_TOPIC" , consumerGroup = "consumer-group01")
@DependsOn(value = "getBeanUtil")
public class MessageConsumer implements RocketMQListener<String> {

    private UtilizationService utilizationService = GetBeanUtil.getBean(UtilizationService.class);

    //onMessage方法多线程同步执行
    @Override
    public  void onMessage(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Utilization utilization = objectMapper.readValue(json, Utilization.class);
            utilizationService.add(utilization);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}