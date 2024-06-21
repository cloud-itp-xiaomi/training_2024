package com.example.xiaomi1coll.tools;

import com.example.xiaomi1coll.entity.Logs;
import com.example.xiaomi1coll.logStorage.LogStorageFactory;
import com.example.xiaomi1coll.logStorage.LogStorageStrategy;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件监控监听器类，负责监听指定文件的内容变化，并在内容变化时将新增内容封装为日志实体上报给server。
 */
public class LogFileListener extends FileAlterationListenerAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(LogFileListener.class);
    private final File file;
    private long lastModified;
    private long lastLength;
    private final String hostname;
    private final LogStorageStrategy logStorageStrategy;

    /**
     * 构造函数，初始化文件监控监听器。
     *
     * @param file        要监听的文件。
     * @param hostname    主机名。
     * @param logStorage  日志存储方式（"local_file" 或 "mysql"）。
     */
    public LogFileListener(File file, String hostname, String logStorage) {
        this.file = file;
        this.lastModified = file.lastModified();
        this.lastLength = file.length();
        this.hostname = hostname;
        this.logStorageStrategy = LogStorageFactory.getLogStorageStrategy(logStorage);
    }

    @Override
    public void onFileChange(File file) {
        if (this.file.equals(file)) {
            logger.info("File changed: {}", file.getAbsolutePath());
            // 文件变更的逻辑
            try {
                if (file.lastModified() > lastModified) {
                    List<String> newLogs = readNewLines(file, lastLength);
                    Logs logs = new Logs();
                    logs.setHostname(hostname);

                    // 将文件路径中的反斜杠替换为正斜杠
                    String normalizedFilePath = file.getAbsolutePath().replace("\\", "/");
                    logs.setFile(normalizedFilePath);

                    logs.setLogs(newLogs);

                    // 调用上报接口
                    logStorageStrategy.storeLog(logs);

                    // 更新指针
                    lastModified = file.lastModified();
                    lastLength = file.length();
                }
            } catch (IOException e) {
                logger.error("Error reading file: {}", file.getAbsolutePath(), e);
            }
        }
    }

    /**
     * 读取文件中从上次读取位置到当前文件末尾的新内容。
     *
     * @param file       要读取的文件。
     * @param lastLength 上次读取的文件长度，用于确定从何处开始读取新内容。
     * @return 包含新增内容的字符串列表。
     * @throws IOException 如果在读取文件时发生 I/O 错误。
     */
    private List<String> readNewLines(File file, long lastLength) throws IOException {
        List<String> newLines = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(lastLength);
            String line;
            while ((line = raf.readLine()) != null) {
                String decodedLine = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();
                if (!decodedLine.isEmpty()) {
                    // 不添加空行，即出现了换行的情况下
                    newLines.add(decodedLine);
                }
            }
        }
        return newLines;
    }
}
