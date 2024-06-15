package com.example.mi1.service.impl;

import com.example.mi1.dao.ESGeneral.ESGeneralDao;
import com.example.mi1.service.ESIndexInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
@Service
@Slf4j
public class ESIndexInitServiceImpl implements ESIndexInitService {
    private final ESGeneralDao esGeneralDao;

    @Autowired
    public ESIndexInitServiceImpl(ESGeneralDao esGeneralDao) {
        this.esGeneralDao = esGeneralDao;
    }

    private void initIndex(String indexName, String mappingPath) throws IOException {
        // 构建 mapping
        ClassPathResource classPathResource = new ClassPathResource(mappingPath);
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }

        // 创建索引
        if (esGeneralDao.existIndex(indexName)) {
            log.info("[{}] exist.", indexName);
            esGeneralDao.deleteIndex(indexName);
            log.info("[{}] has removed", indexName);
        }
        esGeneralDao.createIndex(indexName, builder.toString());
    }

    @Override
    public void initLOGIndex(String indexName) throws IOException {
        initIndex(indexName, LOG_JSON);
    }
}
