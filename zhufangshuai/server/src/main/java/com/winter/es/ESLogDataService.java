package com.winter.es;

import com.winter.req.LogQueryReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * ES存储的持久层数据
 * */
@Service
public class ESLogDataService {

    @Resource
    private ESLogDataDao esLogDataDao;


    /**
     * 查询索引库中全部的数据
     * */
    public Iterable<ESLogData> findAll(){
        Iterable<ESLogData> all = esLogDataDao.findAll();
        return all;
    }

    /**
     * 条件查询
     * 根据主机名和文件名查询索引库中的文件
     * */
    public List<ESLogData> findByHostnameAndFile(LogQueryReq logQueryReq){
        String hostname = logQueryReq.getHostname();
        String file = logQueryReq.getFile();
        List<ESLogData> logDataList = esLogDataDao.findByHostnameAndFile(hostname, file);
        return logDataList;
    }

    /**
     * 向索引库中插入一条数据
     * */
    public void add(ESLogData esLogData){
        ESLogData data = esLogDataDao.save(esLogData);
    }
    
}
