package com.lx.server.pojo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class ResUtilizationTest {

    @Test
    void addValue() {
        ResUtilization resUtilization = new ResUtilization();
        resUtilization.setValues(new ArrayList<>());
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        int size = resUtilization.getValues().size();
        resUtilization.addValue(utilization);
        int size1 = resUtilization.getValues().size();
        assert size1 == size + 1;
    }
}