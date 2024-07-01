package com.winter.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消费日志的存储方式，定时更新日志的存储存储方式
 * */
@Service
@RocketMQMessageListener(consumerGroup = "storage_consumer", topic = "LOG_STORAGE")
public class LogStorageConsumer implements RocketMQListener<MessageExt> {

    private static String storage;  //日志的存储方式，静态成员变量，方便其他类访问

    public static String getStorage() {
        return storage;
    }

    public static void setStorage(String storage) {
        LogStorageConsumer.storage = storage;
    }

    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] messageExtBody = messageExt.getBody();
        storage = new String(messageExtBody);
        System.out.println("消费消息队列中的存储方式：" + storage);
    }

}
