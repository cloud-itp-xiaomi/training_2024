package org.qiaojingjing.collector;

import org.qiaojingjing.constants.Param;
import org.qiaojingjing.entity.Metric;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class CollectMem {
    private static final Map<String, Long> memMap = new HashMap<>();
    private static BigDecimal memUsed;
    private static BigDecimal memUsedDividend;
    private static BigDecimal memUsedDivisor;
    private static final BigDecimal common = new BigDecimal(100);
    private static final Metric mem = new Metric();

    public static Metric collect() throws IOException {
        String hostname = Files.readString(Paths.get("/etc/hostname"))
                                                .trim();
        File fileMem = new File("/proc/meminfo");
        InputStream inputStream = new FileInputStream(fileMem);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            String[] value = split[1].trim().split("\\D+");
            if (split[0].equals(Param.MEM_TOTAL)
                    || Param.MEM_FREE.equals(split[0])
                    || Param.BUFFERS.equals(split[0])
                    || Param.CACHED.equals(split[0])) {
                memMap.put(split[0],
                           Long.parseLong(value[0]));
            }
        }
        reader.close();
        inputStream.close();

        Long memTotal = memMap.get(Param.MEM_TOTAL);
        Long memFree = memMap.get(Param.MEM_FREE);
        Long buffers = memMap.get(Param.BUFFERS);
        Long cached = memMap.get(Param.CACHED);
        memUsedDividend = BigDecimal.valueOf(memTotal - memFree - buffers - cached);
        memUsedDivisor = BigDecimal.valueOf(memTotal);
        memUsed = common.multiply(memUsedDividend.divide(memUsedDivisor, 4, RoundingMode.HALF_UP));

        mem.setMetric(Param.MEM);
        mem.setEndpoint(hostname);
        mem.setStep(60L);
        mem.setValue(memUsed.doubleValue());
        mem.setTags(null);

        return mem;
    }
}