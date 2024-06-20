package com.lx.server.store;

import com.lx.server.service.ConfigMessageConsumeService;
import com.lx.server.store.impl.LocalLogStorage;
import com.lx.server.store.impl.MysqlLogStorage;
import org.springframework.stereotype.Repository;

@Repository
public class LogStorageFactory {

    //获取存储类型
    public LogStorage getLogStorage() {
        String logStorage = ConfigMessageConsumeService.logStorage;
        if("local_file".equals(logStorage)) {
            return LocalLogStorage.getLocalStorage();
        }else {
            return MysqlLogStorage.getMysqlLogStorage();
        }
    }
}
