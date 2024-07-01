package com.winter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winter.config.CFGConfig;
import com.winter.enums.MQTopicEnum;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * 监听日志的存储方式
 * */
@DisallowConcurrentExecution
public class StorageMonitor implements Job {

    @jakarta.annotation.Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 待优化：有些代码不需要每次都重复执行
     * */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            //获取cfg.json文件的路径  --->  得到是一个classpath路径
            Properties properties = new Properties();
            InputStream in = StorageMonitor.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(in);
            String cfg_path = properties.getProperty("cfgConfig.file.path");  //获取cfg文件的路径

            //根据cfg文件路径解析文件内容
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(cfg_path);
            File file = resource.getFile();
            ObjectMapper mapper = new ObjectMapper();
            CFGConfig cfgConfig = mapper.readValue(file, CFGConfig.class); //将配置文件的内容解析到cfgConfig对象中

            //将具体的存储方式上传到消息队列
            SendResult sendResult = rocketMQTemplate.syncSend(MQTopicEnum.LOG_STORAGE.getCode(), cfgConfig.getLog_storage());
            System.out.println(sendResult);
            System.out.println("更新存储方式到消息队列。。。");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
