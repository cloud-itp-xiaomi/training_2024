package org.example.log;

import org.example.rest.DataSender;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.nio.file.*;
import java.time.Instant;
import java.util.concurrent.*;

public class LogWatching {
    private final String logFilePath;
    private final String fileName;
    private long lastReadPosition = 0;
    private final DataSender dataSender;
    private final long delay = 10000; // 10秒延迟
    private final ConfigReader configReader;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final BlockingQueue<Boolean> logQueue = new LinkedBlockingQueue<>();

    private volatile boolean watching = false;

    public LogWatching(String logFilePath, DataSender dataSender, ConfigReader configReader) {
        this.logFilePath = logFilePath;
        this.fileName = logFilePath.substring(logFilePath.lastIndexOf("/") + 1);
        this.dataSender = dataSender;
        this.configReader = configReader;
    }

    public void setLastReadPosition(long lastReadPosition) {
        this.lastReadPosition = lastReadPosition;
    }

    public void startWatching() {
        if (!watching) {
            watching = true;
            scheduler.execute(this::watchLogFile);
            scheduler.scheduleAtFixedRate(this::collectLogs, 0, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void stopWatching() {
        watching = false;
        scheduler.shutdown();
    }

    private void watchLogFile() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path logDir = Paths.get(logFilePath).getParent();
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (watching) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path modifiedFile = (Path) event.context();
                        if (modifiedFile.toString().equals(fileName)) {
                            logQueue.offer(true);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException e) {
            if (watching) {
                e.printStackTrace();
            }
        }
    }

    private void collectLogs() {
        if (logQueue.isEmpty()) {
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(logFilePath, "r")) {
            raf.seek(lastReadPosition);
            String line;
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            Log log = new Log();
            log.setHostname(hostName);
            log.setFile(logFilePath);
            log.setTimestamp(Instant.now().getEpochSecond());

            StringBuilder content = new StringBuilder();
            while ((line = raf.readLine()) != null) {
                content.append(line);
                if (content.length() >= 1024 * 1024) {
                    log.setLog(content.toString());
                    dataSender.sendLogData(log);
                    content.delete(0, content.length());
                }
            }
            log.setLog(content.toString());
            dataSender.sendLogData(log);
            lastReadPosition = raf.getFilePointer();
            configReader.updateLastReadPosition(lastReadPosition);
            logQueue.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
