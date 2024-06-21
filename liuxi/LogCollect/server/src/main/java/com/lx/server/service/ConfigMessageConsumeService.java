package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

@Service
@Data
@RocketMQMessageListener(topic = "" , consumerGroup = "consumer-group01")
public class ConfigMessageConsumeService implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    public static String logStorage = "mysql";//默认使用本地文件存储日志

    //onMessage方法多线程同步执行
    @Override
    public  void onMessage(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            logStorage = objectMapper.readTree(json).asText();// 将 JSON 字符串转换为普通字符串
            if("local_file".equals(logStorage)) {
                logStorage = "local_file";
                System.out.println("now the storage method returns to local_file");
            }else if("mysql".equals(logStorage)) {
                logStorage = "mysql";
                System.out.println("now the storage method returns to mysql");
            }else {
                System.out.println("you had written the wrong storage method!!!");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        try {
            consumer.subscribe("HOST_CONFIG_TOPIC", "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}