package com.lx.collector.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@Data
public class CfgConfig {
    private List<String> files;  //配置文件中包含的日志文件
    private String log_storage;  //存储方法
}
