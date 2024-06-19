package com.lx.collector.pojo;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogFileListenerTest {

    @Test
    void readNewLogs() {
        File file = new File("E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\java\\com\\lx\\collector\\log\\a.log");
        try (FileWriter writer = new FileWriter(file,true)) {
            Long lastPosition = file.length();
            writer.write("hello,world");
            writer.flush();
            List<String> logs = LogFileListener.readNewLogs(file, lastPosition);
            String log = logs.get(0);
            assert "hello,world".equals(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}