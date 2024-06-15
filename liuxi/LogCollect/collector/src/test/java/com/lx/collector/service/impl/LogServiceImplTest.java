package com.lx.collector.service.impl;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogServiceImplTest {

    private LogServiceImpl service = new LogServiceImpl();
    private String filePath = "E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\java\\com\\lx\\collector\\log\\a.log";

    @Test
    void monitorLogFile() {
        service.startMonitor();

        //此处还要添加断言

    }

    @Test
    void readAndReport() {
        Map<String, Long> lastPositions = new HashMap<>();
        lastPositions.put(filePath , 0L);
        service.setLastPositions(lastPositions);
        Path path = Paths.get(filePath);
        service.readAndReport(path);

        assertNotNull(service.getLastPositions());
        assertTrue(service.getLastPositions().containsKey(filePath));

    }


    @Test
    void testMonitorLogFile() {
    }
}