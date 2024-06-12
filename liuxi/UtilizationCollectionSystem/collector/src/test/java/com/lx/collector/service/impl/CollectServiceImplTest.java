package com.lx.collector.service.impl;

import com.lx.collector.service.CollectService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectServiceImplTest {

    private CollectService service = new CollectServiceImpl();

    @Test
    void collectCPU() {
        double cpuUtilization = service.collectCPU("src/test/java/com/lx/collector/service/impl/stat");
        System.out.println(cpuUtilization);
    }
    @Test
    void collectMem() {
        double memUtilization = service.collectMem("src/test/java/com/lx/collector/service/impl/meminfo");
        System.out.println(memUtilization);
    }
}