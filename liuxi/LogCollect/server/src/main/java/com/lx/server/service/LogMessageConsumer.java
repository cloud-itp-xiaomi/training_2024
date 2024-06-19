package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.mapper.LogMapper;
import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.Utilization;
import com.lx.server.store.LogStorage;
import com.lx.server.store.LogStorageFactory;
import com.lx.server.utils.GetBeanUtil;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "HOST_LOG_TOPIC" , consumerGroup = "consumer-group02")
@DependsOn(value = "getBeanUtil")
public class LogMessageConsumer implements RocketMQListener<String> {

    private LogStorageFactory logStorageFactory = new LogStorageFactory();

    //onMessage方法多线程同步执行
    @Override
    public  void onMessage(String json) {
        LogStorage logStorage = logStorageFactory.getLogStorage();
        try {
                ObjectMapper objectMapper = new ObjectMapper();
                LogMessage logMessage = objectMapper.readValue(json, LogMessage.class);
                logStorage.storeLog(logMessage);
                System.out.println("successfully store the logs !!!");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

