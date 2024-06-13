package com.txh.xiaomi2024.work.service.service.impl;

import com.txh.xiaomi2024.work.service.bean.Log;
import com.txh.xiaomi2024.work.service.dao.LogMysqlMapper;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.service.LogQueryService;
import com.txh.xiaomi2024.work.service.service.LogUploadMysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author txh
 */
@Service
public class LogUploadMysql implements LogUploadMysqlService {
    private final RedisDao redisDao;
    private final LogMysqlMapper logMysqlMapper;
    private final LogQueryService logQueryService;

    @Autowired
    public LogUploadMysql(RedisDao redisDao,
                          LogMysqlMapper logMysqlMapper,
                          LogQueryService logQueryService) {
        this.redisDao = redisDao;
        this.logMysqlMapper = logMysqlMapper;
        this.logQueryService = logQueryService;
    }

    @Override
    public String upload(String hostname,
                         String file,
                         List<String> log,
                         long lastUpdateTime) {
        // 先区检索hostname主机的file文档是否已经存储在数据库
        Log logFile = logQueryService.getLogFromMysql(
                hostname,
                file);
        String logs = log.toString();
        Log log1 = new Log(
                hostname,
                file,
                logs,
                lastUpdateTime);
        // 若存在就直接更新
        if (logFile != null) {
            logMysqlMapper.updateLog(log1);
            // 同时在redis删除旧数据,新增新数据
            redisDao.lRemove(
                    "log",
                    1,
                    logFile);
            redisDao.lPush(
                    "log",
                    log1);
        }
        // 否则新增
        else {
            logMysqlMapper.insertLog(log1);
            // 获取新添加的数据，存储到redis
            Log log2 = logMysqlMapper.queryLog(
                    hostname,
                    file);
            // 将最新的数据存储到redis
            setLatestInRedis(
                    "log",
                    log2);
        }

        return "上报数据并完成存储！";
    }

    @Override
    public long getLastUpdateTime(String hostname,
                                  String file) {
        Log logResult = null;
        // 先去redis缓存查询
        List<Object> list = redisDao.lRange(
                "log",
                0,
                redisDao.lSize("log"));
        for (Object o : list) {
            Log log = (Log) o;
            if (hostname.equals(log.getHostname())
                    && file.equals(log.getFile())) {
                logResult = log;
                break;
            }
        }
        // 再去mysql查询
        if (logResult == null) {
            Log log = logMysqlMapper.queryLog(
                    hostname,
                    file);
            if (log == null) {
                return 0;
            }
            logResult = log;
        }

        return logResult.getLast_update_time();
    }

    /**
     * 将最新的10条数据存储到redis
     * @param key 对应存储的key
     * @return
     */
    private void setLatestInRedis(String key,
                                  Log log) {
        redisDao.lPush(
                key,
                log);
        long size = redisDao.lSize(key);
        // 如果redis中存储的数据量大于10，就删除最开始插入的数据
        if (size > 10) {
            redisDao.leftPop(key);
        }
    }
}
