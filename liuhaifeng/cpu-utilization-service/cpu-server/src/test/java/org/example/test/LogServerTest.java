package org.example.test;

import org.example.common.utils.JSONParseUtil;
import org.example.controller.LogController;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.pojo.entity.LogConfigEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务测试类
 *
 * @author liuhaifeng
 * @date 2024/06/10/2:59
 */
@SpringBootTest
public class LogServerTest {

    @Autowired
    private LogController logController;

    @Test
    public void parseJSONConfigTest() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile("src/main/java/org/example/config/config.json");
        System.out.println(logConfigEntity);
    }

    @Test
    public void logUploadTest() {
        List<LogUploadDTO> logUploadDTOList = new ArrayList<>();
        LogUploadDTO logUploadDTO = new LogUploadDTO();
        logUploadDTO.setHostname("my-computer");
        logUploadDTO.setFile("/home/work/a.log");
        List<String> logs = new ArrayList<>();
        logs.add("2024-05-16 10:11:51 +08:00 This is a log");
        logs.add("2024-05-16 10:11:51 +08:00 This is another log");
        logUploadDTO.setLogs(logs);

        LogUploadDTO logUploadDTO2 = new LogUploadDTO();
        logUploadDTO2.setHostname("my-computer");
        logUploadDTO2.setFile("/home/work/b.log");
        List<String> logs2 = new ArrayList<>();
        logs2.add("2024-05-16 10:11:51 +08:00 ERROR tcp_ping::metric src/metric.rs:95 - Report one chunk done, error: Connection refused");
        logs2.add("2024-05-16 10:11:51 +08:00 ERROR tcp_ping::metric src/metric.rs:95 - Report one chunk done, error: Connection refused");
        logUploadDTO2.setLogs(logs2);

        logUploadDTOList.add(logUploadDTO);
        logUploadDTOList.add(logUploadDTO2);

        logController.upload(logUploadDTOList);
    }
}
