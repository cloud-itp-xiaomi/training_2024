package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.pojo.Utilization;
import com.lx.server.utils.GetBeanUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "HOST_UTILIZATION_TOPIC" , consumerGroup = "consumer-group03")
@DependsOn(value = "getBeanUtil")
public class MessageConsumeService implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

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

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        try {
            consumer.subscribe("HOST_UTILIZATION_TOPIC", "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}