package com.lx.server.store;

import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogResult;

public interface LogStorage {

    boolean storeLog(LogMessage logMessage);
    LogResult queryLog(String hostName, String file);

}
