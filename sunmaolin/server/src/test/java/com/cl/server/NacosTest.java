package com.cl.server;

import com.alibaba.fastjson.JSON;
import com.cl.server.config.LogStorageConfig;
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
 * log测试类
 *
 * @author: tressures
 * @date: 2024/6/13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes =ServerApplication.class)
public class NacosTest {

    @Resource
    private LogStorageConfig logStorageConfig;

    @Test
    public void nacosTest(){
        String log_storage = logStorageConfig.getLogStorage();
        log.info("log_storage:{}",log_storage);
    }
}
