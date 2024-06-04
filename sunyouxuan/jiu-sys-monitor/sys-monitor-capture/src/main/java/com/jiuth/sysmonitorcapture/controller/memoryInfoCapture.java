package com.jiuth.sysmonitorcapture.controller;

import java.io.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class memoryInfoCapture {
    public static void main(String[] args) throws FileNotFoundException {
        // This demonstrates the use of Duration class
        final var period = Duration.ofSeconds(1);
        new Timer().schedule(new MemoryUtilizationTask(), 0, period.toMillis());
    }

    /* By extending the TimerTask abstract class, we're able to use the
     * Timer scheduling class */
    static class MemoryUtilizationTask extends TimerTask {

        private final String MEMINFO_FILE = "/proc/meminfo";
        private final NumberFormat percentFormatter;
        private BufferedReader meminfoReader;

        public MemoryUtilizationTask() throws FileNotFoundException {
            this.percentFormatter = NumberFormat.getPercentInstance();
            percentFormatter.setMaximumFractionDigits(2);
            this.meminfoReader = new BufferedReader(new FileReader(MEMINFO_FILE));
        }

        @Override
        public void run() {
            try {
                String line;
                long totalMemory = 0;
                long availableMemory = 0;

                while ((line = meminfoReader.readLine()) != null) {
                    if (line.startsWith("MemTotal:")) {
                        totalMemory = parseMemoryValue(line);
                    } else if (line.startsWith("MemAvailable:")) {
                        availableMemory = parseMemoryValue(line);
                    }
                }

                long usedMemory = totalMemory - availableMemory;
                double memoryUsage = (double) usedMemory / totalMemory;

                // Output total memory, used memory, and memory usage percentage
                System.out.println("Total Memory: " + totalMemory + " KB");
                System.out.println("Used Memory: " + usedMemory + " KB");
                System.out.println("Memory Usage: " + percentFormatter.format(memoryUsage));

                // Reset the reader to the beginning of the file
                meminfoReader.close();
                this.meminfoReader = new BufferedReader(new FileReader(MEMINFO_FILE));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        private long parseMemoryValue(String line) {
            String[] parts = line.split("\\s+");
            return Long.parseLong(parts[1]);
        }
    }

}
