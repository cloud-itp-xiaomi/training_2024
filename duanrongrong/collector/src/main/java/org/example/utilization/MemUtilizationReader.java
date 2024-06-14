package org.example.utilization;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class MemUtilizationReader {
    private final NumberFormat percentFormatter;
    private final RandomAccessFile memPointer;

    public MemUtilizationReader(String filePath) throws IOException {
        this.percentFormatter = new DecimalFormat("#0.0");
        this.percentFormatter.setMaximumFractionDigits(2);
        this.memPointer = new RandomAccessFile(filePath, "r");
    }

    public Float getMemUtilization() throws IOException {
        String line;
        Map<String, Long> memInfoMap = new HashMap<>();
        while ((line = memPointer.readLine()) != null) {
            if (line.startsWith("MemTotal:") || line.startsWith("MemFree:")
                    || line.startsWith("Buffers:") || line.startsWith("Cached:")) {
                String[] parts = line.split("\\s+");
                memInfoMap.put(parts[0], Long.parseLong(parts[1]));
            }
        }
        long memTotal = memInfoMap.getOrDefault("MemTotal:", 0L);
        long memFree = memInfoMap.getOrDefault("MemFree:", 0L);
        long buffers = memInfoMap.getOrDefault("Buffers:", 0L);
        long cached = memInfoMap.getOrDefault("Cached:", 0L);
        memPointer.seek(0);
        Float memUtilization = (1 - ((float) memTotal - (float) memFree - (float) buffers - (float) cached) / (float) memTotal) * 100;
        return Float.valueOf(percentFormatter.format(memUtilization));
    }
}
