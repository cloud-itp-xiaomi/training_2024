package com.lx.collector.impl;

import com.lx.collector.service.CollectService;
import com.lx.collector.service.impl.CollectServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CollectServiceImplTest {

    private CollectService service = new CollectServiceImpl();

    @Test
    void collectCPU() {
        double cpuUtilization = service.collectCPU("src/test/java/com/lx/collector/impl/stat");
        assertEquals(2.9, cpuUtilization);
    }
    @Test
    void collectMem() {
        double cpuUtilization = service.collectMem("src/test/java/com/lx/collector/impl/meminfo");
        assertEquals(25.5, cpuUtilization);

    }
}