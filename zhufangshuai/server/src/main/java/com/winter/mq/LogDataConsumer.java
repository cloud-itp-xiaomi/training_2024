package com.winter.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winter.domain.LogData;
import com.winter.factory.LogStore;
import com.winter.factory.LogStoreFactory;
import com.winter.req.LogUploadReq;
import com.winter.utils.SnowUtil;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 监听消息队列中的日志数据
 * 消费的组为default，主题为LOG_DATA
 *
 * 具体的存储方式由存储工厂实现
 * */
@Service
@RocketMQMessageListener(consumerGroup = "data_consumer", topic = "LOG_DATA")
public class LogDataConsumer implements RocketMQListener<MessageExt> {


    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        String log_data = new String(body);
        System.out.println("消费消息队列中的数据：" + log_data);

        try {
            //将json格式的字符串，转换为相应的对象LogUploadReq
            ObjectMapper objectMapper = new ObjectMapper();
            LogUploadReq data = objectMapper.readValue(log_data, LogUploadReq.class);

            System.out.println(data);
            System.out.println(data.getLogs().toString());

            //获取存储方式
            String store = LogStorageConsumer.getStorage();
            System.out.println("当前的存储方式：" + store);

            //根据存储方式返回具体的日志存储方案
            LogStore storageMethod = LogStoreFactory.getStorageMethod(store);

            //封装LogData,LogData的logs类型为String类型
            LogData logData = new LogData();
            logData.setId(SnowUtil.getSnowflakeNextIdStr());
            logData.setHostname(data.getHostname());
            logData.setFile(data.getFile());
            logData.setLogs(data.getLogs().toString());  //List转String

            //调用存LogStore的存储方案，存储数据
            storageMethod.storeData(logData);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
