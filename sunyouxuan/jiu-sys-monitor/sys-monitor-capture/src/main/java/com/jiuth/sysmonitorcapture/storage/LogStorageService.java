package com.jiuth.sysmonitorcapture.storage;

import java.util.List;
import java.util.Map;

public interface LogStorageService {
    void storeLogs(Map<String, List<String>> fileLogs) throws Exception;

}
