package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.pojo.LogMessage;
import com.lx.server.store.LogStorage;
import com.lx.server.store.LogStorageFactory;
import com.lx.server.utils.GetBeanUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "" , consumerGroup = "consumer-group02")
@DependsOn(value = "getBeanUtil")
public class LogMessageConsumeService implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    private LogStorageFactory logStorageFactory = GetBeanUtil.getBean(LogStorageFactory.class);//存储工厂

    //onMessage方法多线程同步执行
    @Override
    public  void onMessage(String json) {
        LogStorage logStorage = logStorageFactory.getLogStorage();
        try {
                ObjectMapper objectMapper = new ObjectMapper();
                LogMessage logMessage = objectMapper.readValue(json, LogMessage.class);
                logStorage.storeLog(logMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        try {
            consumer.subscribe("HOST_LOG_TOPIC", "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}

