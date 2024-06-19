package com.example.mi1.service;

import com.example.mi1.dao.ESGeneral.ESGeneralDao;
import com.example.mi1.dao.constant.ESConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SysInitService implements ApplicationRunner {
    private final ESIndexInitService esIndexInitService;
    private final ESGeneralDao esGeneralDao;

    @Autowired
    public SysInitService(ESIndexInitService esIndexInitService, ESGeneralDao esGeneralDao) {
        this.esIndexInitService = esIndexInitService;
        this.esGeneralDao = esGeneralDao;
    }

    @Override
    public void run(ApplicationArguments args) {
        esIndexDataInit();
        startMachine();
    }

    /**
     * es 索引初始化
     * 已有改索引则不初始化
     */
    private void esIndexDataInit() {
        try {
            esGeneralDao.deleteIndex(ESConst.Index.LOG.value());
            if (!esGeneralDao.existIndex(ESConst.Index.LOG.value())) {
                esIndexInitService.initLOGIndex(ESConst.Index.LOG.value());
                log.info("init log index finished");
            }
        } catch (IOException e) {
            log.error("索引初始化失败 [{}]", e.getMessage());
        }
    }

    /**
     *  启动机器
     */
    private void startMachine() {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 20, 1L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
        // 测试mysql
//        tpe.execute(new DockerTask("container_1_test"));
//        tpe.execute(new DockerTask("container_2_test"));
//        tpe.execute(new DockerTask("container_3_test"));

        // 测试es
        tpe.execute(new DockerTask("container_4_test"));
        tpe.execute(new DockerTask("container_5_test"));
        tpe.execute(new DockerTask("container_6_test"));
    }
}
