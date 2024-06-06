package com.winter.controller;

import com.winter.domain.LogData;
import com.winter.enums.StatusEnum;
import com.winter.factory.LogStore;
import com.winter.factory.LogStoreFactory;
import com.winter.req.LogQueryReq;
import com.winter.resp.CommonResp;
import com.winter.resp.LogDataResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听上传的日志信息的controller
 * 由于消息的监听使用了MQ，所以上传接口"/log/upload"没有在控制层实现，而是放在mq包中实现
 *
 * 所以在次数，只提供查询接口
 * */
@RestController
@RequestMapping("/log")
public class LogDataController{

    /**
     * 日志查询接口
     * 最后CommResp中的content内容为LogDataResp
     * */
    @GetMapping("/query")
    public CommonResp query(LogQueryReq logQueryReq){
        CommonResp resp = new CommonResp();
        LogDataResp logDataResp = new LogDataResp();
        logDataResp.setHostname(logQueryReq.getHostname());
        logDataResp.setFile(logQueryReq.getFile());
        List<String> logs = new ArrayList<>();

        //应该查询三种方式存储的日志全部的结果
        //查询MySQL
        LogStore mysql = LogStoreFactory.getStorageMethod("mysql");
        List<LogData> mysql_data = mysql.queryData(logQueryReq);
        System.out.println("查询了MySQL的数据：" + mysql_data);
        //封装到logs中
        for (LogData logData : mysql_data){  //这里遗留了一个问题，开头和结尾的日志分别带有"["和"]"
            String log = logData.getLogs();
            String[] log_split = log.split(",");
            for (int i = 0; i < log_split.length; i++){
                logs.add(log_split[i]);
            }
        }

        //查询es
        LogStore es = LogStoreFactory.getStorageMethod("elasticsearch");
        List<LogData> es_data = es.queryData(logQueryReq);
        System.out.println("查询了ES的数据：" + es_data);
        for (LogData logData : es_data){
            String log = logData.getLogs();
            String[] log_split = log.split(",");
            for (int i = 0; i < log_split.length; i++){
                logs.add(log_split[i]);
            }
        }

        //查询Local_file
        LogStore local_file = LogStoreFactory.getStorageMethod("local_file");
        List<LogData> local_file_data = local_file.queryData(logQueryReq);
        System.out.println("查询了local_file的数据：" + local_file_data);
        for (LogData logData : local_file_data){
            String log = logData.getLogs();
            String[] log_split = log.split(",");
            for (int i = 0; i < log_split.length; i++){
                logs.add(log_split[i]);
            }
        }

        //封装resp，还有错误的情况需要处理
        logDataResp.setLogs(logs);
        resp.setContent(logDataResp);
        resp.setCode(StatusEnum.QUERY_SUCCESS.getCode());
        resp.setMessage("ok");
        return resp;
    }
}
