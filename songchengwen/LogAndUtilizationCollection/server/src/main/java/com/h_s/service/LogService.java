package com.h_s.service;

import java.util.List;
import java.util.Map;

public interface LogService {
    void saveLogs(String hostname, String file, List<String> logs);
    List<Map<String, String>> queryLogs(String hostname, String file);
}
