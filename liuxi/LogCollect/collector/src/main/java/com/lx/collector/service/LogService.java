package com.lx.collector.service;

import java.nio.file.Path;

public interface LogService {

     void readAndReport(Path filePath); //读取新增的日志并上报
     void readConfigAndReport();//读取配置文件存储方式并上报
}
