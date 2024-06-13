package org.example.utilization;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CPUUtilizationReader {
    private final String STAT_FILE_HEADER = "cpu  ";
    private final NumberFormat percentFormatter;
    private final RandomAccessFile statPointer;

    public CPUUtilizationReader(String filePath) throws IOException {
        this.percentFormatter = new DecimalFormat("#0.0");
        this.percentFormatter.setMaximumFractionDigits(2);
        this.statPointer = new RandomAccessFile(filePath, "r");
    }

    public Float getCpuUtilization() throws IOException {
        String[] values = statPointer.readLine().substring(STAT_FILE_HEADER.length()).split(" ");
        long idleTime = Long.parseUnsignedLong(values[3]);
        long totalTime = 0L;
        for (String value : values) {
            totalTime += Long.parseUnsignedLong(value);
        }
        Double cpuUtilization = (1 - ((double) idleTime) / totalTime) * 100;
        statPointer.seek(0);
        return Float.valueOf(percentFormatter.format(cpuUtilization));
    }
}

