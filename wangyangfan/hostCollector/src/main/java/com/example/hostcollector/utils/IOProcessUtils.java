package com.example.hostcollector.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOProcessUtils {

    /** @return CPU占用率 */
    public static double getCPUUtilization() throws IOException {
        String cpuInfoPath = "/proc/stat";
        return parseCPUUtilization(readFirstLineByPath(cpuInfoPath));
    }

    /** @return mem占用率 */
    public static double getMemUtilization() throws IOException {
        String memInfoPath = "/proc/meminfo";
        return parseMemUtilization(readFirstNLineByPath(memInfoPath, 5));
    }

    private static String readFirstLineByPath(String path) throws IOException {
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }

    private static String readFirstNLineByPath(String path, int linesToRead) throws IOException {
        if (linesToRead <= 0) {
            throw new IllegalArgumentException("linesToRead must be greater than 0");
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < linesToRead) {
                contentBuilder.append(line).append("\n");
                count++;
            }
        }
        return contentBuilder.toString().trim();
    }

    /** @return CPU占用率 */
    private static double parseCPUUtilization(String str) {
        str = str.substring(4).stripLeading();
        int total = 0;
        double idle = 0;
        int index = 0;
        for (String value : str.split(" ")) {
            if (index == 3)
                idle = Integer.parseInt(value);
            total += Integer.parseInt(value);
            index++;
        }
        idle /= total;
        return idle;
    }

    /** @return mem占用率 */
    private static double parseMemUtilization(String str){
        List<Long> msgMap = extractMemoryValue(str);
        long memTotal = msgMap.get(0);
        long memFree = msgMap.get(1);
        long buffers = msgMap.get(3);
        long caches = msgMap.get(4);
        return (double) (memTotal - memFree - buffers - caches) / memTotal;
    }

    /** @return 内存信息正则匹配 */
    private static List<Long> extractMemoryValue(String memInfo) {
        String pattern = "\\b\\d+\\b";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(memInfo);
        List<Long> map = new ArrayList<>();
        while(m.find()){
            map.add(Long.parseLong(m.group()));
        }
        return map;
    }

}
