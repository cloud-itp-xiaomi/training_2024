package com.jiuth.sysmonitorcapture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileMonitorService {

    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;

    public FileMonitorService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.executorService = Executors.newFixedThreadPool(2); // 使用两个线程监听两个文件
    }

    public void startMonitoring(List<String> files) {
        for (String file : files) {
            executorService.submit(() -> {
                try {
                    monitorFile(file);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public void monitorFile(String filePath) throws IOException, InterruptedException {
        Path path = Paths.get(filePath);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Start monitoring file: " + filePath);

        // 记录上次读取的位置
        long lastPosition = Files.size(path);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    Path changed = (Path) event.context();
                    if (changed.equals(path.getFileName())) {
                        System.out.println("File changed: " + path);

                        // 使用 RandomAccessFile 定位到上次读取的位置
                        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r");
                             FileChannel channel = raf.getChannel()) {
                            // 定位到上次读取的位置
                            channel.position(lastPosition);

                            // 读取新增的内容并输出
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            while (channel.read(buffer) != -1) {
                                buffer.flip();
                                while (buffer.hasRemaining()) {
                                    System.out.print((char) buffer.get());
                                }
                                buffer.clear();
                            }

                            // 更新上次读取的位置
                            lastPosition = channel.position();
                        }
                    }
                }
            }
            key.reset();
        }
    }

}
