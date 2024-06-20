package com.cl.server;

import com.alibaba.fastjson.JSON;
import com.cl.server.entity.Status;
import com.cl.server.pojo.DTO.StatusQueryDTO;
import com.cl.server.pojo.VO.StatusResp;
import com.cl.server.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * status测试类
 *
 * @author: tressures
 * @date: 2024/6/13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class StatusTest {

    @Resource
    private StatusService statusService;

    @Test
    public void uploadTest(){
        List<Status> statusList = new ArrayList<>();
        Status cpu = new Status();
        cpu.setMetric("cpu.used.percent");
        cpu.setEndpoint("my-computer");
        cpu.setTimestamp(1715765640L);
        cpu.setStep(60L);
        cpu.setValue(60.1);
        statusList.add(cpu);
        Status mem = new Status();
        mem.setMetric("mem.used.percent");
        mem.setEndpoint("my-computer");
        mem.setTimestamp(1715765640L);
        mem.setStep(60L);
        mem.setValue(35.0);
        statusList.add(cpu);
        statusService.uploadMetrics(statusList);
    }

    @Test
    public void queryTest(){
        StatusQueryDTO statusQueryDTO = new StatusQueryDTO();
        statusQueryDTO.setEndpoint("my-computer");
        statusQueryDTO.setMetric("cpu.used.percent");
        statusQueryDTO.setStart_ts(1718434016L);
        statusQueryDTO.setEnd_ts(1718517372L);
        List<StatusResp> statusRespList =statusService.queryMetrics(statusQueryDTO);
        log.info("logInfoVO:{}", JSON.toJSONString(statusRespList));
    }

}
