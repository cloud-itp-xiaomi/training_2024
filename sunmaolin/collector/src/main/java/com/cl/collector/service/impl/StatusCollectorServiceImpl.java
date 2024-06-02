package com.cl.collector.service.impl;

import com.cl.collector.service.StatusCollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Service("statusCollectorService")
@Slf4j
public class StatusCollectorServiceImpl implements StatusCollectorService {


    @Override
    public Double getCpuUsage() throws IOException {
        FileReader fileReader = new FileReader("/proc/stat");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        long totalTime = 0;
        long idleTime = 0;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("cpu ")) {
                String[] tokens = line.split("\\s+");
                totalTime = Arrays.stream(tokens)
                        .skip(1)
                        .mapToLong(Long::parseLong)
                        .sum();
                idleTime = Long.parseLong(tokens[4]);
            }
        }
        return 100 * (totalTime - idleTime)*1.0 / totalTime;
    }

    @Override
    public Double getMemUsage() throws IOException {
        FileReader fileReader = new FileReader("/proc/meminfo");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        long totalMemory = 0;
        long freeMemory = 0;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("MemTotal:")) {
                totalMemory = Long.parseLong(line.split("\\s+")[1]);
            } else if (line.startsWith("MemFree:")) {
                freeMemory = Long.parseLong(line.split("\\s+")[1]);
            }
        }
        return 100 * (totalMemory - freeMemory)*1.0 / totalMemory;
    }
}
