package com.winter.controller;

import com.winter.es.ESLogData;
import com.winter.es.ESLogDataService;
import com.winter.req.LogQueryReq;
import com.winter.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class test {

    @Resource
    private ESLogDataService esLogDataService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/test")
    public void test(@RequestBody LogQueryReq logQueryReq){
        System.out.println(logQueryReq);

        //查询索引库中的所有数据
        System.out.println("\n============查询索引库中的所有数据================\n");
        Iterable<ESLogData> all = esLogDataService.findAll();
        for (ESLogData data : all){
            System.out.println(data);
        }
        System.out.println("\n============查询索引库中的所有数据================\n");

        System.out.println("\n==============插入数据到索引库==================\n");
        ESLogData esLogData = new ESLogData();
        esLogData.setId(SnowUtil.getSnowflakeNextId());
        esLogData.setHostname("jnt");
        esLogData.setFile("/a/b/c.log");
        esLogData.setLogs("这是一条测试日志");
        esLogDataService.add(esLogData);
        System.out.println("\n==============插入数据到索引库==================\n");

        System.out.println("\n============根据条件查询索引库中的数据================\n");
        List<ESLogData> esLogDataServiceByHostnameAndFile = esLogDataService.findByHostnameAndFile(logQueryReq);
        for (ESLogData data : esLogDataServiceByHostnameAndFile){
            System.out.println(data);
        }
        System.out.println("\n============根据条件查询索引库中的数据================\n");
    }
}
