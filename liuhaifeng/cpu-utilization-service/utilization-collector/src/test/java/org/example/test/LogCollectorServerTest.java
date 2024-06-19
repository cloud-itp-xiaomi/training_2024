package org.example.test;

import org.example.LogMonitorApplicationRunner;
import org.example.pojo.entity.LogConfigEntity;
import org.example.utils.JSONParseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 日志采集服务测试类
 *
 * @author liuhaifeng
 * @date 2024/06/13/22:49
 */
@SpringBootTest
public class LogCollectorServerTest {

    @Autowired
    private LogMonitorApplicationRunner logMonitorApplicationRunner;

    @Test
    public void JSONParseUtilTest() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile("config/config.json");
        System.out.println(logConfigEntity);
        System.out.println(logConfigEntity.getFiles().get(0));
        System.out.println(logConfigEntity.getFiles().get(1));
        System.out.println(logConfigEntity.getLogStorage());
    }

    @Test
    public void getLogCollectorFilesTest(){
        List<String> logCollectorFiles = logMonitorApplicationRunner.getLogCollectorFiles();
        System.out.println(logCollectorFiles);
    }
}
