package com.jiuth.sysmonitorcapture;


import com.jiuth.sysmonitorcapture.collector.LogCollector;
import com.jiuth.sysmonitorcapture.config.SysLogConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {LogCollector.class, SysLogConfig.class})
public class LogMonitorTest {

    @Autowired
    private LogCollector logCollector;

    @Autowired
    private SysLogConfig config;

    @Test
    public void testLogCollectorInitialization() throws IOException {
        assertNotNull(logCollector);
        assertNotNull(config);
        assertFalse(config.getFiles().isEmpty());
    }

    @Test
    public void testConfigFiles() throws IOException {
        List<String> files = config.getFiles();
        assertEquals(2, files.size());
        assertTrue(files.contains("/home/work/a.log"));
        assertTrue(files.contains("/home/work/b.log"));
    }
}
