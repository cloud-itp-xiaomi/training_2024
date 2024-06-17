package com.txh.xiaomi2024.work.collector.service.impl;

import com.txh.xiaomi2024.work.collector.service.LogCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LogCollectServiceIml implements LogCollectService {
    @Override
    public List<String> collectLogs(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            List<String> logContents = new ArrayList<>();
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            logContents.add(sb.toString());

            return logContents;
        } catch (IOException e) {
            log.error("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return null;
    }
}
