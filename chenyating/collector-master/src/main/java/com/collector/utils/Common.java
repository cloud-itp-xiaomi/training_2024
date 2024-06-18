package com.collector.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
@Slf4j
public class Common {
    // 创建线程池
    // 创建一个单例的ScheduledExecutorService对象。是一个可以安排任务在指定延迟后执行或周期性执行的线程池服务。
    // 使用volatile修饰executorService变量，保证多线程环境下的可见性
    // 当一个线程修改了executorService的值后，其他线程能够立即看到修改后的值。
    public static volatile ScheduledExecutorService executorService;
    {
        if(executorService == null){
            synchronized (this){
                if(executorService == null){
                    executorService = Executors.newScheduledThreadPool(1);
                }
            }
        }
    }

    public Common(){}

    public String getHostName() {
        try {
            // 获取本地主机的InetAddress实例
            InetAddress localHost = InetAddress.getLocalHost();
            // 获取主机名
            return localHost.getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getSystemType(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "Windows";
        } else if (os.contains("linux")) {
            return "Linux";
        }else{
            return "其他";
        }
    }

    public void createFile(String filePath){
        File file=new File(filePath);
        // 第一个条件检查file所指对象的父目录是否为非空。即确认文件路径中是否有定义父目录
        // 第二个条件进一步检查，如果父目录存在（即上一步判断为非空），那么这个父目录是否实际存在于文件系统中
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            // 如果父目录不存在，创建它
            boolean mkdirs = file.getParentFile().mkdirs();
            if(!mkdirs){
                log.error("父目录创建失败!");
            }
        }

        try {
            // 尝试创建文件
            if (file.createNewFile()) {
                log.info("文件创建成功： " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
        }
    }
}
