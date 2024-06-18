package com.collector.utils;

import com.collector.bean.request.CollectorLogUploadRequest;
import com.collector.service.ICollectorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class SaveLogByMysql implements SaveLogParent{

    @Resource
    private Common common;
    @Resource
    private ICollectorLogService iCollectorLogService;

    @Override
    public void saveLogs(List<String> files) {
        log.info("使用mysql存储日志文件信息");

        List<CollectorLogUploadRequest> requests = new ArrayList<>();
        String logInfoFilePath = "/opt/logs/collector/info/log_info.log";
        String logErrorFilePath = "/opt/logs/collector/error/log_error.log";
        String destinationLogInfoFilePath = files.get(0);
        String destinationLogErrorFilePath = files.get(1);
        requests.add(readToMysql(logInfoFilePath,destinationLogInfoFilePath));
        requests.add(readToMysql(logErrorFilePath,destinationLogErrorFilePath));

        iCollectorLogService.upload(requests);
    }

    // 文件读取，写进list，再转数组，存入logs
    private CollectorLogUploadRequest readToMysql(String logFilePath, String destinationLogFilePath) {
        CollectorLogUploadRequest request = new CollectorLogUploadRequest();
        List<String> logList = new ArrayList<>();

        try (BufferedReader reader = getBufferedReader(logFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                logList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!logList.isEmpty()){
            // 它会创建一个新的数组，其类型与你提供的数组相同，但大小刚好足够容纳列表中的所有元素
            request.setLogs(logList.toArray(new String[0]));
        }

        request.setHostname(common.getHostName());
        request.setFile(destinationLogFilePath);
        return request;
    }

    private BufferedReader getBufferedReader(String logInfoPath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(logInfoPath));
    }
}
