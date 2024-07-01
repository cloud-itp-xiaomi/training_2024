package org.example.log;

import org.example.rest.DataSender;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.*;
import java.time.Instant;

public class LogWatching {
    private final String logFilePath; // 配置的日志文件路径
    private final String fileName;
    private long lastReadPosition = 0; // 上次读取的位置
    private final DataSender dataSender;
    LogWatching(String logFilePath,DataSender dataSender) {
        this.logFilePath = logFilePath;
        this.fileName = logFilePath.substring(logFilePath.lastIndexOf("/") + 1);
        this.dataSender = dataSender;
    }

    void watchLogFile(){
        try {
            // 创建了一个 WatchService 对象 watchService，用于监视文件系统的变化
            // 注册了 logDir 目录的 ENTRY_MODIFY 事件，即监视目录中文件的修改
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path logDir = Paths.get(logFilePath).getParent();
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedFile = (Path) event.context(); // 获取事件发生的文件路径。
                        if (modifiedFile.toString().equals(fileName)) { // 判断文件路径是否与指定的fileName相同。
                            collectLogs(logFilePath);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectLogs(String logFilePath) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(logFilePath, "r")) {
            raf.seek(lastReadPosition);
            String line;
            while ((line = raf.readLine()) != null) {
                InetAddress localHost = InetAddress.getLocalHost(); // 获取本地主机的 InetAddress 实例
                String hostName = localHost.getHostName(); // 获取主机名
                Log log = new Log();
                log.setHostname(hostName);
                log.setFile(logFilePath);
                log.setTimestamp(Instant.now().getEpochSecond());
                log.setLog(line);
                dataSender.sendLogData(log);
            }
            lastReadPosition = raf.getFilePointer();
        }
    }
}
