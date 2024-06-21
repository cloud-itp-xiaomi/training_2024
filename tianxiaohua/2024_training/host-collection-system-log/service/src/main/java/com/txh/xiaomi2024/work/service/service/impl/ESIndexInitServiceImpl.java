package com.txh.xiaomi2024.work.service.service.impl;

import com.txh.xiaomi2024.work.service.dao.ESDao;
import com.txh.xiaomi2024.work.service.service.ESIndexInitService;
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
    private final ESDao esDao;

    @Autowired
    public ESIndexInitServiceImpl(ESDao esDao) {
        this.esDao = esDao;
    }

    @Override
    public void initLogDataIndex(String indexName) throws IOException {
        initIndex(
                indexName,
                LOG_JSON);
    }

    private void initIndex(String indexName,
                           String mappingPath) throws IOException {
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
        if (esDao.existIndex(indexName)) {
            log.info("[{}] exist.", indexName);
            esDao.deleteIndex(indexName);
            log.info("[{}] has removed", indexName);
        }
        esDao.createIndex(
                indexName,
                builder.toString());
    }
}
