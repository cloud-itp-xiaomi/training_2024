package com.lx.server.service;

import com.lx.server.pojo.ResUtilization;
import com.lx.server.pojo.Result;
import com.lx.server.pojo.Utilization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilizationServiceTest {

    @Autowired
    private UtilizationService utilizationService;
    @Test
    void add() {
        Utilization utilization = getUtilization();
        utilizationService.add(utilization);
    }
    @Test
    void queryByMetric() {
        Result result = utilizationService.queryByMetric("6853817426de", "cpu.used.percent",611062457L, 611062557L);
        System.out.println(result);
    }
    @Test
    void query() {
        Result result = utilizationService.query("6853817426de",611062457L, 611062557L);
        System.out.println(result);
    }
    @Test
    void getDataByMetric() {
        List<Utilization> utilizationList = new ArrayList<>();
        utilizationList.add(getUtilization());
        ResUtilization[] res = utilizationService.getDataByMetric(utilizationList, "mem.used.percent");
        for(ResUtilization resUtilization : res){
            System.out.println(resUtilization);
        }
    }
    @Test
    void getData() {
        List<Utilization> utilizationList = new ArrayList<>();
        utilizationList.add(getUtilization());
        ResUtilization[] res = utilizationService.getData(utilizationList);
        for(ResUtilization resUtilization : res){
            System.out.println(resUtilization);
        }
    }
    public Utilization getUtilization() {
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        return utilization;
    }

}