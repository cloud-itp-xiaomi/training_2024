package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.pojo.LogMessage;
import com.lx.server.utils.GetBeanUtil;
import lombok.Data;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@Data
@RocketMQMessageListener(topic = "HOST_CONFIG_TOPIC" , consumerGroup = "consumer-group03")
public class ConfigMessageConsumer implements RocketMQListener<String> {

    private String logStorage = "mysql";//默认使用mysql数据库存储日志

    //onMessage方法多线程同步执行
    @Override
    public  void onMessage(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            logStorage = objectMapper.readTree(json).asText();// 将 JSON 字符串转换为普通字符串
            if("local_file".equals(logStorage)) {
                System.out.println("now the storage method returns to local_file");
            }else if("mysql".equals(logStorage)) {
                System.out.println("now the storage method returns to mysql");
            }else {
                System.out.println("you had written the wrong storage method!!!");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}