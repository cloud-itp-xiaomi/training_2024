package com.example.mi1.service;

import com.example.mi1.dao.ESGeneral.ESGeneralDao;
import com.example.mi1.dao.constant.ESConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
}
