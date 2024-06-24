package com.example.xiaomi1.service;

import com.example.xiaomi1.entity.Log;
import com.example.xiaomi1.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            // 规范格式
            writer.write(log.toFormattedString() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 查询方法，都查
    public List<Log> getLog(String hostname, String file) {
        // 从MySQL中查询
        List<Log> logs = logMapper.getLog(hostname, file);

        // 使用Set来跟踪已经存在的日志
        Set<String> existingLogs = new HashSet<>();
        if (logs != null) {
            for (Log log : logs) {
                existingLogs.add(log.getLog());
            }
        }

        // 从文件中查询
        List<Log> fileLogs = queryLogsFromFile(hostname, file);
        if (logs != null && logs.isEmpty()) {
            // MySQL中没有数据
            return fileLogs;
        }

        for (Log log : fileLogs) {
            if (!existingLogs.contains(log.getLog())) {
                if (logs != null) {
                    logs.add(log);
                }
            }
        }

        return logs;
    }

    // 只查MySQL
    public List<Log> getLogByMysql(String hostname, String file) {
        return logMapper.getLog(hostname, file);
    }

    // 只查文件
    public List<Log> getLogByLocal(String hostname, String file) {
        return queryLogsFromFile(hostname, file);
    }

    // 从文件中查询日志
    private List<Log> queryLogsFromFile(String hostname, String file) {
        List<Log> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("local_logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log log = parseLog(line);
                if (log != null && log.getHostname().equals(hostname) && log.getFile().equals(file)) {
                    logs.add(log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    // 解析日志行
    private Log parseLog(String logLine) {
        // 使用正则表达式匹配并提取hostname, file, log
        Pattern pattern = Pattern.compile("hostname:(.*?), file:(.*?), log:(.*)");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.matches() && matcher.groupCount() == 3) {
            Log log = new Log();
            log.setHostname(matcher.group(1).trim());
            log.setFile(matcher.group(2).trim());
            log.setLog(matcher.group(3).trim());
            return log;
        }
        return null;
    }
}
