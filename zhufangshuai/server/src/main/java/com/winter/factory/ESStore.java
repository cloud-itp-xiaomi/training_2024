package com.winter.factory;

import com.winter.domain.LogData;
import com.winter.es.ESLogData;
import com.winter.es.ESLogDataService;
import com.winter.req.LogQueryReq;
import com.winter.utils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 采用ES存储数据
 * */
public class ESStore implements LogStore{

    private ESLogDataService esLogDataService = BeanUtils.getBean(ESLogDataService.class);

    @Override
    public void storeData(LogData logData) {
        //将LogData封装成ESLogData
        ESLogData esLogData = new ESLogData();
        esLogData.setId(Long.valueOf(logData.getId()));
        esLogData.setHostname(logData.getHostname());
        esLogData.setFile(logData.getFile());
        esLogData.setLogs(logData.getLogs());

        //调用es存储接口存储数据
        esLogDataService.add(esLogData);
    }

    @Override
    public List<LogData> queryData(LogQueryReq logQueryReq) {
        //调用es查询索引库的接口
        List<ESLogData> byHostnameAndFile = esLogDataService.findByHostnameAndFile(logQueryReq);

        //将ESLogData封装成LogData
        List<LogData> logDataList = new ArrayList<>();
        for (ESLogData esLogData : byHostnameAndFile){
            LogData logData = new LogData();
            logData.setId(esLogData.getId().toString());
            logData.setHostname(esLogData.getHostname());
            logData.setFile(esLogData.getFile());
            logData.setLogs(esLogData.getLogs());
            logDataList.add(logData);
        }
        return logDataList;
    }
}
