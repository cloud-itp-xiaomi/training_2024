package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Log;
import com.example.xiaomi1.entity.Logs;
import com.example.xiaomi1.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    // 保存到数据库
    public void saveLogMysql(Log log) {
        System.out.println(log);
        logMapper.insert(log);
    }

    // 保存到本地文件
    public void saveLogLocal(Log log) {
        System.out.println(log);
        try (FileWriter writer = new FileWriter("local_logs.txt", true)) {
            writer.write(log.toFormattedString() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 查询方法
    public List<Log> getLog(String hostname, String file) {

        return null;
    }



}
