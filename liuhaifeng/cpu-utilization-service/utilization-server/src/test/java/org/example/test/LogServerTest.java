package org.example.test;

import org.example.common.utils.JSONParseUtil;
import org.example.controller.LogController;
import org.example.enums.LogSaveTypeEnum;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.example.handler.LocalFileLogSaveHandle;
import org.example.handler.MySQLLogSaveHandler;
import org.example.pojo.entity.LogConfigEntity;
import org.junit.jupiter.api.Assertions;
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

    @Autowired
    private MySQLLogSaveHandler mySQLLogSaveHandler;

    @Autowired
    private LocalFileLogSaveHandle localFileLogSaveHandle;

    @Test
    public void parseJSONConfigTest() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile("config/config.json");
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

    @Test
    public void logQueryTest1() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setHostname("my-computer");
        logQueryDTO.setFile("/home/work/a.log");
        Result<LogQueryVO> query = logController.query(logQueryDTO);
        System.out.println(query);
    }

    @Test
    public void logQueryTest2() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setHostname("my-computer");
        logQueryDTO.setFile("/home/work/b.log");
        Result<LogQueryVO> query = logController.query(logQueryDTO);
        System.out.println(query);
    }

    @Test
    public void logQueryByHostnameTest() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setHostname("my-computer");
        Result<LogQueryVO> query = logController.query(logQueryDTO);
        System.out.println(query);
    }

    @Test
    public void logsQueryByFileTest() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setFile("/home/work/b.log");
        Result<LogQueryVO> query = logController.query(logQueryDTO);
        System.out.println(query);
    }

    @Test
    public void logSaveTypeEnumTest() {
        LogSaveTypeEnum mysql = LogSaveTypeEnum.getByCode(1);
        LogSaveTypeEnum localFile = LogSaveTypeEnum.getByCode(2);
        LogSaveTypeEnum mysql1 = LogSaveTypeEnum.getByValue("mysql");
        LogSaveTypeEnum localFile1 = LogSaveTypeEnum.getByValue("local_file");
        Assertions.assertEquals(LogSaveTypeEnum.MYSQL, mysql);
        Assertions.assertEquals(LogSaveTypeEnum.MYSQL, mysql1);
        Assertions.assertEquals(LogSaveTypeEnum.LOCAL_FILE, localFile);
        Assertions.assertEquals(LogSaveTypeEnum.LOCAL_FILE, localFile1);
    }

    @Test
    public void mySQLLogSaveHandlerUploadTest() {
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
        mySQLLogSaveHandler.save(logUploadDTOList);
    }

    @Test
    public void mySQLLogSaveHandlerQueryTest() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setHostname("my-computer");
        logQueryDTO.setFile("/home/work/b.log");
        LogQueryVO query = mySQLLogSaveHandler.query(logQueryDTO);
        System.out.println(query);
    }

    @Test
    public void localFileLogSaveHandlerUploadTest() {
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
        localFileLogSaveHandle.save(logUploadDTOList);
    }

    @Test
    public void localFileLogSaveHandlerQueryTest() {
        LogQueryDTO logQueryDTO = new LogQueryDTO();
        logQueryDTO.setHostname("my-computer");
        logQueryDTO.setFile("/home/work/b.log");
        LogQueryVO query = localFileLogSaveHandle.query(logQueryDTO);
        System.out.println(query);
    }
}
