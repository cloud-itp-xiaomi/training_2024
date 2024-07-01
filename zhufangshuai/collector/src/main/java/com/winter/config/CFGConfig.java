package com.winter.config;

import java.util.List;

/**
 * cfg.json配置文件对应的配置类
 * */
public class CFGConfig {
    private List<String> files;  //需要监听的配置文件所在的全路径集合
    private String log_storage;  //存储方式

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getLog_storage() {
        return log_storage;
    }

    public void setLog_storage(String log_storage) {
        this.log_storage = log_storage;
    }
}
