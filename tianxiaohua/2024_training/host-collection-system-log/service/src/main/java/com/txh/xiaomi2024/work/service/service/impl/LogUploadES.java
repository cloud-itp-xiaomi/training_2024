package com.txh.xiaomi2024.work.service.service.impl;

import com.txh.xiaomi2024.work.service.constant.ESConst;
import com.txh.xiaomi2024.work.service.dao.ESDao;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.domian.po.LogDocument;
import com.txh.xiaomi2024.work.service.service.LogQueryService;
import com.txh.xiaomi2024.work.service.service.LogUploadESService;
import com.txh.xiaomi2024.work.service.util.IdGeneratorUtil;
import io.lettuce.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
@Slf4j
public class LogUploadES implements LogUploadESService {
    private final RedisDao redisDao;
    private final LogQueryService logQueryService;
    private final ESDao esDao;

    @Autowired
    public LogUploadES(RedisDao redisDao,
                       LogQueryService logQueryService,
                       ESDao esDao) {
        this.redisDao = redisDao;
        this.logQueryService = logQueryService;
        this.esDao = esDao;
    }

    @Override
    public String upload(String hostname,
                         String file,
                         List<String> logs,
                         long lastUpdateTime) {
        LogDocument logDocument = LogDocument.builder()
                .id(IdGeneratorUtil.standAloneSnowFlake())
                .hostname(hostname)
                .file(file)
                .logs(logs)
                .last_update_time(lastUpdateTime)
                .build();
        LogDocument document = logQueryService.getLogFromES(
                hostname,
                file);

        if (document != null) {
            logDocument.setId(document.getId());
            // 同时在redis删除旧数据
            try {
                redisDao.lRemove("log_es", 1, document);
                redisDao.lPush("log_es", logDocument);
            } catch (RedisCommandTimeoutException | RedisConnectionException | RedisCommandExecutionException |
                     RedisCommandInterruptedException e) {
                throw new RedisException(e);
            }
        } else { // 否则就将新数据存入redis
            setLatestInRedis("log_es", logDocument);
        }

        log.info("归档日志信息 [{}] ", logDocument);
        logArchive(logDocument);
        return "ok";
    }

    @Override
    public long getLastUpdateTime(String hostname,
                                  String file) {
        LogDocument logResult = null;
        // 先去redis缓存查询
        try {
            List<Object> list = redisDao.lRange(
                    "log_es",
                    0,
                    redisDao.lSize("log_es"));
            for (Object o : list) {
                LogDocument document = (LogDocument) o;
                if (hostname.equals(document.getHostname())
                        && file.equals(document.getFile())) {
                    logResult = document;
                    break;
                }
            }
        } catch (RedisCommandTimeoutException | RedisConnectionException | RedisCommandExecutionException |
                 RedisCommandInterruptedException e) {
            throw new RedisException(e);
        }

        // 再去es查询
        if (logResult == null) {
            LogDocument logDocument = logQueryService.getLogFromES(
                    hostname,
                    file);
            if (logDocument == null) {
                return 0;
            }
            logResult = logDocument;
        }

        return logResult.getLast_update_time();
    }

    /**
     * 将最新的10条数据存储到redis
     * @param key 对应存储的key
     * @return
     */
    private void setLatestInRedis(String key,
                                  LogDocument logDocument) {
        try {
            redisDao.lPush(key, logDocument);
            redisDao.trim(key, -10,-1);
        } catch (RedisCommandTimeoutException | RedisConnectionException | RedisCommandExecutionException |
                 RedisCommandInterruptedException e) {
            throw new RedisException(e);
        }
    }

    /**
     * 将数据保存到es
     * @param document
     */
    private void logArchive(LogDocument document) {
        // 判断id是否存在以及封装在document.getId()内部
        String archiveId = String.valueOf(document.getId());

        try {
            if (esDao.existsById(ESConst.Index.LOG.value(), archiveId)) {
                esDao.updateData(
                        ESConst.Index.LOG.value(),
                        document,
                        archiveId);
            }
            esDao.putData(
                    document,
                    ESConst.Index.LOG.value(),
                    archiveId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
