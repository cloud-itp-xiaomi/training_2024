package com.lx.server.controller;

import com.lx.server.pojo.LogResult;
import com.lx.server.pojo.Result;
import com.lx.server.store.LogStorage;
import com.lx.server.store.LogStorageFactory;
import com.lx.server.utils.GetBeanUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/log")
@DependsOn(value = "getBeanUtil")
@CrossOrigin
public class LogController {

    private LogStorageFactory logStorageFactory = GetBeanUtil.getBean(LogStorageFactory.class);//存储工厂

    //查询日志
    @GetMapping("query")
    public LogResult query(@RequestParam("hostName") String hostName,
                           @RequestParam("file") String file) {
        LogStorage logStorage = logStorageFactory.getLogStorage();
        return logStorage.queryLog(hostName, file);
    }
}
