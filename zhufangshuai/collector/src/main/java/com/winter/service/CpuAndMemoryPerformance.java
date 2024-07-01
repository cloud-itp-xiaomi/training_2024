package com.winter.service;

import com.winter.feign.ServerFeign;
import com.winter.req.UploadReq;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


/**
 * 该类需要定时采集数据，实现quartz的Job类
 * */

@DisallowConcurrentExecution  //禁止任务并发执行
public class CpuAndMemoryPerformance implements Job {

    private static String HOSTNAME;

    private static UploadReq[] uploadReqs = new UploadReq[2];

    @Resource
    private ServerFeign serverFeign;

    public CpuAndMemoryPerformance(){  //在构造方法中初始化
        for (int i = 0; i < uploadReqs.length; i++) {
            uploadReqs[i] = new UploadReq();
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取当前系统的时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddhhmmss");  //不要年份
        String formattedNow  = LocalDateTime.now().format(formatter);
        int collect_time = Integer.parseInt(formattedNow);  //将采集的时间转换为整数

        System.out.println("\n\n当前的时间：" + formattedNow);

        try {
            //cpu使用率和内存使用率
            double cpuUsage = getCpuUsage();
            double memoryUsage = getMemoryUsage();

            System.out.println("cpu利用率：" + cpuUsage + "%");
            System.out.println("内存利用率：" + memoryUsage + "% \n");

            //获取主机名称
            if (HOSTNAME == null){
                HOSTNAME = getHostname();
            }

            System.out.println("主机名称：" + HOSTNAME);

            //封装数据,将数据封装为数组的形式
            //cpu相关数据
            uploadReqs[0].setMetric("cpu.used.percent");
            uploadReqs[0].setEndpoint(HOSTNAME);
            uploadReqs[0].setTimestamp(collect_time);
            uploadReqs[0].setStep(60);
            uploadReqs[0].setValue(cpuUsage);

            //memory相关数据
            uploadReqs[1].setMetric("mem.used.percent");
            uploadReqs[1].setEndpoint(HOSTNAME);
            uploadReqs[1].setTimestamp(collect_time);
            uploadReqs[1].setStep(60);
            uploadReqs[1].setValue(memoryUsage);

            System.out.println(uploadReqs[0]);
            System.out.println(uploadReqs[1]);

            //跨服务调用server
            serverFeign.add(uploadReqs);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 获取CPU的使用率
     *
     * /proc/stat文件的内容：cpu  3357 0 4313 1362393 729 0 94 0 0 0
     * 第一个字段cpu,表示这一行包含了整个系统的cpu信息
     * 第二个字段：用户态（user）：CPU 在用户空间执行用户程序的时间。
     * 第三个字段：Nice 级别用户态（nice）：CPU 在用户空间执行低优先级的用户程序的时间
     * 第四个字段：系统态（system）：CPU 在内核空间执行系统程序的时间。
     * 第五个字段：空闲态（idle）：CPU 空闲的时间。
     * 第六个字段：I/O 等待（iowait）：CPU 在等待 I/O 操作完成的时间。
     * 第七个字段：硬中断（irq）：CPU 在处理硬中断的时间。
     * 第八个字段：软中断（softirq）：CPU 在处理软中断的时间。
     * 第九个字段：虚拟机（steal）：CPU 在虚拟化环境中被虚拟机偷取的时间
     * 第十个字段：客户（guest）：CPU 在虚拟机中运行客户操作系统的时间。
     * 第是一个字段：客户 Nice 级别（guest_nice）：CPU 在虚拟机中运行低优先级的客户操作系统的时间。
     * */
    public double getCpuUsage() throws IOException {
        FileReader fileReader = new FileReader("/proc/stat");
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("cpu ")) {
                String[] tokens = line.split("\\s+");
                long totalTime = Arrays.stream(tokens)
                        .skip(1)  // 跳过第一个字段 "cpu"
                        .mapToLong(Long::parseLong)
                        .sum();
                long idleTime = Long.parseLong(tokens[4]);

                // 计算 CPU 利用率
                double usage = 100 * (totalTime - idleTime) / totalTime;
                return usage;
            }
        }

        return 0.0; // 如果未找到匹配的行，则返回默认值
    }


    /**
     * 获取内存的使用率
     * */
    public double getMemoryUsage() throws IOException {
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

        // 计算内存利用率
        double memoryUsage = 100 * (totalMemory - freeMemory) / totalMemory;
        return memoryUsage;
    }

    /**
     * 获取主机名称
     * */
    public String getHostname() throws IOException{
        String hostname = InetAddress.getLocalHost().getHostName();
        return hostname;
    }
}
