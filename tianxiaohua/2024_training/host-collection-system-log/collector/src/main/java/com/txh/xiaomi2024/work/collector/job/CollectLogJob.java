package com.txh.xiaomi2024.work.collector.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txh.xiaomi2024.work.LogUploadService;
import com.txh.xiaomi2024.work.collector.bean.JsonData;
import com.txh.xiaomi2024.work.collector.service.LogCollectService;
import com.txh.xiaomi2024.work.collector.util.PathUtil;
import com.txh.xiaomi2024.work.collector.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.quartz.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
/**
 * @author txh
 */
@Slf4j
@Service
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CollectLogJob implements Job {
    private LogCollectService logCollectService;

    String LOG_JSON = PathUtil.concat("static",
            "log.json");
    @DubboReference
    private LogUploadService logUploadService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logUploadService = SpringUtil.getBean(LogUploadService.class);
        logCollectService = SpringUtil.getBean(LogCollectService.class);

        String hostname = getHostName();
        log.info("当前主机：{}", hostname);

        // 日志采集
        JsonData jsonData = readJsonData(); // 将json文件与JsonData 实例类映射
        for (String file: jsonData.getFiles()) {
            File logFile = new File(file);

            long currentModifiedTime = logFile.lastModified();
            // 获取存储在数据库中日志文件的最后修改时间
            long lastUpdateTime = logUploadService.oldLogFileUpdateTime(
                    hostname,
                    file,
                    jsonData.getLog_storage());
            // 判断文件是否更新，只有更新了才去上传
            log.info("[{}]lastUpdateTime:{}", file, lastUpdateTime);
            log.info("[{}]currentModifiedTime:{}", file, currentModifiedTime);
            // 文件更新
            if (currentModifiedTime > lastUpdateTime) {
                // 获取文件中的日志
                List<String> logs = logCollectService.collectLogs(file);
                // 上报
                logUploadService.upload(hostname,
                        file,
                        logs,
                        jsonData.getLog_storage(),
                        logFile.lastModified());
            }
        }
    }

    // 获取当前主机名
    private String getHostName(){
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("hostname");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String hostname = br.readLine();
            p.destroy();
            return hostname;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 日志采集的json文件读取
    private JsonData readJsonData(){
        ClassPathResource classPathResource = new ClassPathResource(LOG_JSON);
        JsonData jd = null;
        try {
            InputStream inputStream = classPathResource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            jd = objectMapper.readValue(
                    inputStream,
                    JsonData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jd;
    }
}
