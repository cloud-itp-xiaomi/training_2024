package com.collector.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

@Component
@Slf4j
public class SaveLogByFile implements SaveLogParent{
    @Resource
    private Common common;

    @Override
    public void saveLogs(List<String> files) {
       log.info("使用文件形式存储日志");

       // 先验证配置文件中的日志地址是否存在,如不存在则创建
        files.forEach(filePath-> common.createFile(filePath));

        // 从两个固定的日志文件中读取内容，并将这些内容分别复制到由外部提供的两个目标文件路径中
        String logInfoFilePath = "/opt/logs/collector/info/log_info.log";
        String logErrorFilePath = "/opt/logs/collector/error/log_error.log";

        String destinationLogInfoFilePath = files.get(0);
        String destinationLogErrorFilePath = files.get(1);
        readToWriteLog(logInfoFilePath, destinationLogInfoFilePath);
        readToWriteLog(logErrorFilePath, destinationLogErrorFilePath);
    }

    // 单个文件的读取与写入操作，Try-with-resources语句
    private void readToWriteLog(String logFilePath, String destinationLogFilePath) {
        try (BufferedReader reader = getBufferedReader(logFilePath);
             BufferedWriter writer = getBufferedWriter(destinationLogFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getBufferedReader(String logInfoPath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(logInfoPath));
    }

    private BufferedWriter getBufferedWriter(String logErrorPath) throws IOException {
        return new BufferedWriter(new FileWriter(logErrorPath));
    }

}
