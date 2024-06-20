package com.cl.server;

import com.alibaba.fastjson.JSON;
import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.service.LogStorageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
public class LogTest {

    @Resource
    private LogStorageService logStorageService;

    @Test
    public void uploadTest(){
        List<LogInfoDTO> logInfoDTOS = new ArrayList<>();
        logInfoDTOS.add(new LogInfoDTO( "my-computer", "/home/work/a.log",
                Arrays.asList("2024-05-16 10:11:51 +08:00 This is a log",
                        "2024-05-16 10:11:51 +08:00 This is another log")));
        logInfoDTOS.add(new LogInfoDTO("my-computer", "/home/work/b.log",
                Arrays.asList("2024-05-16 10:11:51 +08:00 ERROR tcp_ping::metric src/metric.rs:95 - Report one chunk done, error: Connection refused",
                        "2024-05-16 10:11:51 +08:00 ERROR tcp_ping::metric src/metric.rs:95 - Report one chunk done, error: Connection refused")));
        logStorageService.uploadLogs(logInfoDTOS);
    }

    @Test
    public void queryTest(){
        LogQueryDTO logQueryDTO = new LogQueryDTO("my-computer", "/home/work/a.log");
        LogInfoVO logInfoVO = logStorageService.queryLogs(logQueryDTO);
        log.info("logInfoVO:{}", JSON.toJSONString(logInfoVO));
    }
}
