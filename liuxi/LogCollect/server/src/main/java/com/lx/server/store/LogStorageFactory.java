package com.lx.server.store;

import com.lx.server.service.ConfigMessageConsumer;
import com.lx.server.store.impl.LocalLogStorage;
import com.lx.server.store.impl.MysqlLogStorage;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = "getBeanUtil")
public class LogStorageFactory {

    private ConfigMessageConsumer configMessageConsumer = new ConfigMessageConsumer();
    //获取存储类型
    public LogStorage getLogStorage() {
        String logStorage = configMessageConsumer.getLogStorage();
        if("local_file".equals(logStorage)) {
            return new LocalLogStorage();
        }else {
            return new MysqlLogStorage();
        }
    }
}
