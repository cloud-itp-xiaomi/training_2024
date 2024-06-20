package com.lx.server.service;

import com.lx.server.mapper.UtilizationMapper;
import com.lx.server.pojo.ResUtilization;
import com.lx.server.pojo.Result;
import com.lx.server.pojo.Utilization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DependsOn(value = "getBeanUtil")
class UtilizationServiceTest {

    @Autowired
    private UtilizationService utilizationService;

    @Autowired
    private UtilizationMapper utilizationMapper;

    boolean equals(Utilization u1 , Utilization u2) {
        boolean res = false;
        if(u1.getId() == u2.getId()
                && u1.getEndpoint().equals(u2.getEndpoint())
                && u1.getMetric().equals(u2.getMetric())
                && u1.getTimestamp().equals(u2.getTimestamp())) {
            res = true;
        }
        return res;
    }

    boolean equalsResUtilization(ResUtilization u1 , ResUtilization u2) {
        boolean res = false;
        Map<String, Object> values1 =  u1.getValues().get(0);
        Map<String, Object> values2 =  u2.getValues().get(0);
        double value1 = (Double)values1.get("value");
        double value2 = (Double)values2.get("value");
        double timeStamp1 = (Long)values1.get("timestamp");
        double timeStamp2 = (Long)values2.get("timestamp");
        if(u1.getMetric().equals(u2.getMetric())
                && Math.abs(value1 - value2) < 0.000001
                && timeStamp1 == timeStamp2) {
            res = true;
        }
        return res;
    }


    @Test
    void add() {
        Utilization utilization = getUtilization();
        utilizationService.add(utilization);
        List<Utilization> list = utilizationMapper.queryByMetric(utilization.getEndpoint(), utilization.getMetric() , utilization.getTimestamp() , utilization.getTimestamp());
        Utilization utilization1 = list.get(0);
        boolean res = equals(utilization1,utilization);
        assert res == true;
    }


    @Test
    void queryByMetric() {
        Result result = utilizationService.queryByMetric("6853817426de", "cpu.used.percent",611062457L, 611062557L);
        assertNotNull(result);
    }

    @Test
    void query() {
        Result result = utilizationService.query("6853817426de",611062457L, 611062557L);
        assertNotNull(result);
    }

    /**
     * 将Utilization类对象列表包装成ResUtilization数组
     */
    @Test
    void getDataByMetric() {
        List<Utilization> utilizationList = new ArrayList<>();
        utilizationList.add(getUtilization());
        ResUtilization[] res = utilizationService.getDataByMetric(utilizationList, "mem.used.percent");
        ResUtilization resUtilization = new ResUtilization();
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Utilization utilization = getUtilization();
        map.put("timestamp" , utilization.getTimestamp());
        map.put("value" , utilization.getValue());
        values.add(map);
        resUtilization.setMetric("mem.used.percent");
        resUtilization.setValues(values);
        boolean result = equalsResUtilization(res[0], resUtilization);
        assert result == true;
    }

    /**
     * 将Utilization类对象列表包装成ResUtilization数组
     */
    @Test
    void getData() {
        List<Utilization> utilizationList = new ArrayList<>();
        utilizationList.add(getUtilization());
        ResUtilization[] res = utilizationService.getData(utilizationList);
        ResUtilization resUtilization = new ResUtilization();
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Utilization utilization = getUtilization();
        map.put("timestamp" , utilization.getTimestamp());
        map.put("value" , utilization.getValue());
        values.add(map);
        resUtilization.setMetric("mem.used.percent");
        resUtilization.setValues(values);
        boolean result = equalsResUtilization(res[1], resUtilization);
        assert result == true;

    }

    /**
     * @return Utilization类实例对象
     */
    public Utilization getUtilization() {
        Utilization utilization = new Utilization();
        utilization.setId(79L);
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        return utilization;
    }
}