package com.lx.collector.service.impl;

import com.lx.collector.service.CollectService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Service
public class CollectServiceImpl implements CollectService {

    /*
     * stat文件首行
     * cpu标志  用户态CPU时间 低优先级用户态CPU时间  内核态CPU时间 空闲时间  等待I/O完成的时间 硬中断时间  软中断时间  虚拟机时间  运行虚拟化客户机的时间  运行虚拟化客户机且优先级较低的时间
     * cpu 10645 127 12015 9844066 634 0 1608 0 0 0
     * CPU利用率计算公式： (1- 空闲时间/总时间) * 100
     */
    @Override
    public Double collectCPU(String proc_stat_path) {

        //初始化cpu利用率
        double cpuUsage = 0;
        StringBuilder collectMsg = new StringBuilder();

        //获取输入流
        try(BufferedReader reader = new BufferedReader(new FileReader(proc_stat_path))) {
            // 读取输入流
            String line;
            if((line = reader.readLine()) != null) {
                collectMsg.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 解析CPU使用率
        String msg = collectMsg.toString();
        String[] cpuFields = msg.split("\\s+");
        if(!cpuFields[0].equals("cpu"))
            return 0.0;

        //计算cpu总时间
        long total = Arrays.stream(cpuFields , 1 , cpuFields.length)
                .mapToLong(Long::parseLong)
                .sum();
        long idle = Long.parseLong(cpuFields[4]);

        //计算cpu利用率
        cpuUsage = (1 - ((double) idle / total)) * 100;
        return Double.parseDouble(String.format("%.1f", cpuUsage));
    }
    /*
     * meminfo文件
     * MemTotal 总内存大小
     * MemFree 空闲内存大小
     * Buffers 缓冲区大小
     * Cached 缓存大小
     * 内存利用率计算公式：100*(MemTotal-MemFree-Buffers-Cached)/MemTotal
     */
    @Override
    public Double collectMem(String proc_mem_path) {

        double memUsage = 0;
        StringBuilder collectMsg = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(proc_mem_path))) {

            // 读取输入流
            String line;
            while ((line = reader.readLine()) != null) {
                collectMsg.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //计算内存使用率
        String[] memFields = collectMsg.toString().split("\\n");
        long memTotal = parseMemStr(memFields[0]);
        long memFree = parseMemStr(memFields[1]);
        long buffers = parseMemStr(memFields[3]);
        long cached = parseMemStr(memFields[4]);

        //计算内存利用率
        memUsage = 100 * (double)(memTotal - memFree - buffers - cached) / memTotal;
        return Double.parseDouble(String.format("%.1f", memUsage));
    }

    //从meminfo文件中的每一行解析出内存数值大小
    private Long parseMemStr(String s) {
        String[] parts = s.split(":");
        if(parts.length >= 2) {
            String value = parts[1].split("\s+")[1].trim();
            return Long.parseLong(value);
        }
        return 0L;
    }
}
