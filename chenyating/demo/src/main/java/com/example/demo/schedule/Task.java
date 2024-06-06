package com.example.demo.schedule;

import com.example.demo.service.MyMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Task {
//    @Autowired
//    private MyMailService myMailService;
//    @Scheduled(cron="0 49 20 16 12 ?") //12月16日20:49:00 定时执行
//    private void sendMail(){
//        myMailService.sendSimpleMail("wustzz@163.com","测试","来自zz的问候");
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("邮件发送时间:" + df.format(new Date()));
//    }
}