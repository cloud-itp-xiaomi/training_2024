package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.example.exception.BaseException;
import org.example.handler.FileListenerFactory;
import org.example.pojo.entity.LogConfigEntity;
import org.example.utils.JSONParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 应用启动后执行监听日志
 *
 * @author liuhaifeng
 * @date 2024/06/14/18:33
 */
@Slf4j
@Component
public class LogMonitorApplicationRunner implements ApplicationRunner {

    @Value("${utilization-collector.config.file-path}")
    private String configFilePath;

    @Autowired
    private FileListenerFactory fileListenerFactory;

    @Override
    public void run(ApplicationArguments args) {
        List<String> logCollectorFiles = getLogCollectorFiles();
        for (String logConfigFile : logCollectorFiles) {
            FileAlterationMonitor monitor;
            try {
                monitor = fileListenerFactory.getMonitor(logConfigFile);
                monitor.start();
                log.info("文件监听器启动成功，开始监测：{}", logConfigFile);
            } catch (IOException e) {
                throw new BaseException("读取日志文件出错");
            } catch (Exception e) {
                throw new BaseException("文件监听器启动失败");
            }
        }
    }

    public List<String> getLogCollectorFiles() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile(configFilePath);
        if (logConfigEntity == null) {
            throw new BaseException("配置文件解析失败");
        }
        return logConfigEntity.getFiles();
    }
}
