package com.lx.server.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResUtilizationTest {

    @Test
    void addValue() {
        ResUtilization resUtilization = new ResUtilization();
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        resUtilization.addValue(utilization);

    }
}