package com.example.hostmonitor.service;

import com.example.hostmonitor.pojo.QueryData;
import com.example.hostmonitor.pojo.QueryMsg;
import com.example.hostmonitor.pojo.UploadData;

import java.util.List;

public interface MonitorService {
    void saveAllData(UploadData memData, UploadData cpuData);

    List<QueryData> queryBetweenTime(QueryMsg queryMsg);
}
