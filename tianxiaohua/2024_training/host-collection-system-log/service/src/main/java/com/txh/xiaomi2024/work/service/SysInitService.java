package com.txh.xiaomi2024.work.service;

import com.txh.xiaomi2024.work.service.constant.ESConst;
import com.txh.xiaomi2024.work.service.dao.ESDao;
import com.txh.xiaomi2024.work.service.service.ESIndexInitService;
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
    private final ESDao esDao;

    @Autowired
    public SysInitService(ESIndexInitService esIndexInitService,
                          ESDao esDao) {
        this.esIndexInitService = esIndexInitService;
        this.esDao = esDao;
    }

    /**
     * 程序启动的时候就进行初始化索引
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        esIndexInit();
    }
    /**
     * es 初始化索引
     */
    private void esIndexInit(){
        try {
            // 初始化数据集
            if (!esDao.existIndex(ESConst.Index.LOG.value())) {
                esIndexInitService.initLogDataIndex(ESConst.Index.LOG.value());
                log.info("init work_log index finished");
            }
        } catch (IOException e) {
            log.error("索引初始化失败 [{}]", e.getMessage());
        }
    }
}
