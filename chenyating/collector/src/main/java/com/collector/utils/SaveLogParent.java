package com.collector.utils;

import java.util.List;

//日志存储父类，抽象出接口
public interface SaveLogParent {
    void saveLogs(List<String> files);
}
