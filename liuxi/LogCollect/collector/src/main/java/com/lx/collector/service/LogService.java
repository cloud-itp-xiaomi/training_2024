package com.lx.collector.service;

import java.nio.file.Path;
import java.util.List;

public interface LogService {

    public void readAndReport(Path filePath); //读取新增的日志
    public void monitorLogFile(Path path); //监听日志文件变化
}
