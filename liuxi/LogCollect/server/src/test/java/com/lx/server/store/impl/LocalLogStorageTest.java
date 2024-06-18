package com.lx.server.store.impl;

import com.lx.server.pojo.LogMessage;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class LocalLogStorageTest {

    private LocalLogStorage localLogStorage = new LocalLogStorage();
    @Test
    void storeLog() {
        LogMessage logMessage = new LogMessage();
        logMessage.setHostName("123");
        logMessage.setFile("a.log");
        List<String> logs = new ArrayList<>();
        logs.add("this is a new log");
        logMessage.setLogs(logs);
        boolean res = localLogStorage.storeLog(logMessage);
        assert true == res;
    }

    @Test
    void createFile() {
    }

    @Test
    void listFiles() {
        List<File> files = localLogStorage.listFiles("123", "a.log");
        System.out.println(files);
        assert 2 == files.size();
    }
}