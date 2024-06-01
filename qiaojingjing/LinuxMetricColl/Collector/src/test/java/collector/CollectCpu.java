package collector;

import org.qiaojingjing.cons.Param;
import org.qiaojingjing.entity.Metric;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CollectCpu {
    private static final Metric cpu = new Metric();

    public static Metric collectTest() throws IOException {
        //String hostname = Files.readString(Paths.get("/etc/hostname"));
        String hostname = "localhost.localdomain";

        //第一次读取
        String line1 = getFirstLine();
        Long[] values1 = string2Long(line1);
        //计算totalCpuTime
        Long sum1 = getSum(values1);

        //等1s再读取
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //第二次读取
        String line2 = getFirstLine2();
        Long[] values2 = string2Long(line2);
        //计算totalCpuTime
        Long sum2 = getSum(values2);

        //计算cpu使用率
        long idle = values2[3] - values1[3];
        long total = sum2 - sum1;

        double value = (double) 100 * (total - idle) / total;
        BigDecimal bd = new BigDecimal(value);
        value = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


        cpu.setEndpoint(hostname);
        cpu.setStep(60L);
        cpu.setMetric(Param.CPU);
        cpu.setValue(value);
        cpu.setTags(null);

        return cpu;
    }

    public static Metric collect() throws IOException {
        String hostname = Files.readString(Paths.get("/etc/hostname"));

        //第一次读取
        String line1 = getFirstLine();
        Long[] values1 = string2Long(line1);
        //计算totalCpuTime
        Long sum1 = getSum(values1);

        //等1s再读取
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //第二次读取
        String line2 = getFirstLine();
        Long[] values2 = string2Long(line2);
        //计算totalCpuTime
        Long sum2 = getSum(values2);

        //计算cpu使用率
        long idle = values2[3] - values1[3];
        long total = sum2 - sum1;

        double value = (double) 100 * (total - idle) / total;

        BigDecimal bd = new BigDecimal(value);
        value = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        cpu.setEndpoint(hostname);
        cpu.setStep(60L);
        cpu.setMetric(Param.CPU);
        cpu.setValue(value);
        cpu.setTags(null);

        return cpu;

    }

    private static String getFirstLine() throws IOException {
        //File file = new File("/proc/stat");
        File file = new File("D:\\桌面\\training_2024\\qiaojingjing\\LinuxMetricColl\\Collector\\src\\main\\resources\\stat1");
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        //只需要取第一行的参数即可
        return br.readLine();
    }

    private static String getFirstLine2() throws IOException {
        //File file = new File("/proc/stat");
        File file = new File("D:\\桌面\\training_2024\\qiaojingjing\\LinuxMetricColl\\Collector\\src\\main\\resources\\stat2");
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        //只需要取第一行的参数即可
        return br.readLine();
    }

    private static Long getSum(Long[] values) {
        Long totalCpuTime = 0L;
        for (Long value : values) {
            totalCpuTime += value;
        }
        return totalCpuTime;
    }

    private static Long[] string2Long(String line) {
        //对line进行加工，读取相应值
        String[] valueStr = line.split("\\s+");
        Long[] values = new Long[valueStr.length-1];//存放数字
        for (int i = 0; i < values.length; i++) {
            values[i] = Long.parseLong(valueStr[i+1]);
        }
        return values;
    }


}
