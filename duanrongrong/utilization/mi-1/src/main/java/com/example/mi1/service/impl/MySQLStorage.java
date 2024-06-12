package com.example.mi1.service.impl;

import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;
import com.example.mi1.mapper.LogMapper;
import com.example.mi1.service.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MySQLStorage implements Storage {
    private final LogMapper logMapper;

    @Autowired
    public MySQLStorage(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public void save(Log logs) {
        try {
            logMapper.insertLog(logs);
        } catch (Exception e) {
            log.info("插入日志--MySQL数据库失败!");
        }
    }

    @Override
    public LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize) {
        try {
            List<Log> results = logMapper.getAllLogs(hostname, file);
            List<String> logs = new ArrayList<>();
            for (Log item : results){
                logs.add(item.getLog());
            }
            return LogQueryVO.builder().logs(logs).pageNum((int) Math.ceil(((double) results.size() / pageSize))).totalNum(results.size()).build();
        } catch (Exception e) {
            log.info("查询日志--MySQL数据库失败!");
        }
        return null;
    }
}
