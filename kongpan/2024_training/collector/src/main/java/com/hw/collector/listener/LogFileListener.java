package com.hw.collector.listener;

import com.hw.collector.client.LogClient;
import com.hw.collector.config.nacos.NacosJsonConfig;
import com.hw.collector.dto.ConfigDTO;
import com.hw.collector.dto.LogEntry;
import com.hw.collector.manager.FileOffsetManager;
import com.hw.collector.utils.CollectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mrk
 * @create 2024-06-06-16:27
 */
@Component
@RequiredArgsConstructor
public class LogFileListener extends FileAlterationListenerAdaptor {

    private final CollectUtil collectUtil;
    private final LogClient logClient;
    private final FileOffsetManager fileOffsetManager;
    private final NacosJsonConfig nacosJsonConfig;
    private List<LogEntry> logEntries = new ArrayList<>();

    @Override
    public void onFileChange(File file) {
        System.out.println("File changed: " + file.getAbsolutePath());
//            processFile(file, collectUtil.getLogStorageType("/cfg.json"));
        processFile(file, nacosJsonConfig.getConfigDTO().getLogStorage());
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("File created: " + file.getAbsolutePath());
//            processFile(file, collectUtil.getLogStorageType("/cfg.json"));
        processFile(file, nacosJsonConfig.getConfigDTO().getLogStorage());
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("File deleted: " + file.getAbsolutePath());
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        // 1. 获取监听文件路径
//            String[] logFilePaths = collectUtil.getLogFilePaths("/cfg.json");
        ConfigDTO configDTO = nacosJsonConfig.getConfigDTO();
        String[] logFilePaths = configDTO.getFiles();
        // 2. 获取 observer 监听目录下的所有文件
        File[] files = fileAlterationObserver.getDirectory().listFiles();
        assert (logFilePaths != null && files != null);

        // 3. 判断 files 中文件的路径是否在 logFilePaths 中
        for (File file : files) {
            if (Arrays.asList(logFilePaths).
                    contains(file.getAbsolutePath().replace("\\", "/"))) {
                // 若在，将其中日志内容进行上传
//                    processFile(file, collectUtil.getLogStorageType("/cfg.json"));
                processFile(file, configDTO.getLogStorage());
            }
        }

    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
        try {
//            String logStorageType = collectUtil.getLogStorageType("/cfg.json");
            String logStorageType = nacosJsonConfig.getConfigDTO().getLogStorage();
            if (!logEntries.isEmpty()) {
                logClient.uploadLogs(logEntries, logStorageType);
                logEntries.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(File file, String storageType) {
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            // 1. 从缓存中取出待处理文件的偏移量
            String filePath = file.getAbsolutePath();
            long offset = fileOffsetManager.getOffset(filePath, storageType);
            // 2. 从偏移位置开始采集
            raf.seek(offset);
            String line;
            StringBuilder logContent = new StringBuilder();
            while ((line = raf.readLine()) != null) {
                logContent.append(line).append(System.lineSeparator());
            }
            // 3. 更新偏移量，保存在缓存中
            fileOffsetManager.setOffset(filePath, storageType, raf.getFilePointer());

            if (!logContent.isEmpty()) {
                LogEntry logEntry = new LogEntry();
                logEntry.setHostname(collectUtil.getIpAddress());
                logEntry.setFile(filePath);
                logEntry.setLog(logContent.toString());
                logEntries.add(logEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
