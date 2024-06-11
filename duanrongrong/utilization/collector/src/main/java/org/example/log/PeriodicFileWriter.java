package org.example.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicFileWriter {

    private final String filePath;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PeriodicFileWriter(String filePath) {
        this.filePath = filePath;
        startWriting();
    }

    private void startWriting() {
        scheduler.scheduleAtFixedRate(this::writeLogEntry, 10, 60, TimeUnit.SECONDS);
    }

    private void writeLogEntry() {
        writeToFile(filePath, "Log path: " + filePath +"  Time: " + getCurrentTime());
    }

    private static void writeToFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), (content + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
