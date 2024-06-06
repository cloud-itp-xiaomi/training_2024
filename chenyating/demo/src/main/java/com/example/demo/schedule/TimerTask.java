package com.example.demo.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimerTask {
    private int count=0;
    @Scheduled( cron="15-30/5 * * * * ?" ) // 表示每5秒执行一次（"/"表示时间步长，也可写为 "0/5 * * * * ?"）
    private void process() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        System.out.println("当前时间:" + df.format(new Date()) );
        System.out.println("定时任务：count=" + (count++) ); //每隔5秒+1
    }

}