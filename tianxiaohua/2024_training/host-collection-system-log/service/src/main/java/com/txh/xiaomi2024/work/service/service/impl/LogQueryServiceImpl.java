package com.txh.xiaomi2024.work.service.service.impl;

import com.txh.xiaomi2024.work.service.bean.Log;
import com.txh.xiaomi2024.work.service.dao.ESDao;
import com.txh.xiaomi2024.work.service.dao.LogMysqlMapper;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.domian.po.LogDocument;
import com.txh.xiaomi2024.work.service.service.LogQueryService;
import com.txh.xiaomi2024.work.service.constant.ESConst;
import com.txh.xiaomi2024.work.service.util.BeanMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogQueryServiceImpl implements LogQueryService {
    private final RedisDao redisDao;
    private final LogMysqlMapper logMysqlMapper;
    private final ESDao esDao;

    @Autowired
    public LogQueryServiceImpl(RedisDao redisDao,
                               LogMysqlMapper logMysqlMapper,
                               ESDao esDao) {
        this.redisDao = redisDao;
        this.logMysqlMapper = logMysqlMapper;
        this.esDao = esDao;
    }

    @Override
    public Log getLogFromMysql(String hostName,
                               String file) {
        Log logResult = null;
        // 先去redis缓存查询
        List<Object> list = redisDao.lRange(
                "log",
                0,
                redisDao.lSize("log"));
        for (Object o : list) {
            Log log = (Log) o;
            if (hostName.equals(log.getHostname())
                    && file.equals(log.getFile())) {
                logResult = log;
                break;
            }
        }
        // 再去mysql查询
        if (logResult == null) {
            Log log = logMysqlMapper.queryLog(
                    hostName,
                    file);
            logResult = log;
        }
        return logResult;
    }

    @Override
    public LogDocument getLogFromES(String hostName,
                                    String file) {
        List<Map<String, Object>> maps = esDao.termByField(
                ESConst.Index.LOG.value(),
                "hostname",
                hostName,
                ESConst.ES_MAX_RESULT_WINDOW);
        List<Map<String, Object>> maps1 = esDao.termByField(
                ESConst.Index.LOG.value(),
                "file",
                file,
                ESConst.ES_MAX_RESULT_WINDOW);
        maps.addAll(maps1);
        if(maps.isEmpty()) return null;
        return BeanMapUtil.mapToBean(
                maps.get(0),
                LogDocument.class);
    }
}
