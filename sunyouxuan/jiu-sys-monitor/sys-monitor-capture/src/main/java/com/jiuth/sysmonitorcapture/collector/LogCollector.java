package com.jiuth.sysmonitorcapture.collector;


import com.jiuth.sysmonitorcapture.config.SysLogConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogCollector {
    @Autowired
    private SysLogConfig config;

    // 记录上次读取的文件位置
    private Map<String, Long> lastReadPosition = new HashMap<>();

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        for (String filePath : config.getFiles()) {
            Path path = Paths.get(filePath).getParent();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            // 初始化读取位置为文件尾部
            long fileSize = Files.size(Paths.get(filePath));
            lastReadPosition.put(filePath, fileSize);
        }

        // 启动一个新线程来监听文件变化，避免阻塞主线程
        new Thread(() -> {
            try {
                watchForChanges(watchService);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void watchForChanges(WatchService watchService) throws IOException, InterruptedException {
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                Path changed = (Path) event.context();
                for (String filePath : config.getFiles()) {
                    if (changed.endsWith(Paths.get(filePath).getFileName())) {
                        processFileChange(filePath);
                    }
                }
            }
            key.reset();
        }
    }

    private void processFileChange(String filePath) throws IOException {
        Path fullPath = Paths.get(filePath);
        long lastPosition = lastReadPosition.get(filePath);
        long size = Files.size(fullPath);

        if (size > lastPosition) {
            try (SeekableByteChannel channel = Files.newByteChannel(fullPath)) {
                channel.position(lastPosition);
                byte[] buffer = new byte[(int) (size - lastPosition)];
                channel.read(ByteBuffer.wrap(buffer));
                String content = new String(buffer);
                System.out.println(content);
                System.out.println("New content from " + filePath + ":");
                sendLogToServer(content);
            }
            lastReadPosition.put(filePath, size);
        }
    }

    private void sendLogToServer(String content) {
        // 发送日志内容到服务器
    }
}