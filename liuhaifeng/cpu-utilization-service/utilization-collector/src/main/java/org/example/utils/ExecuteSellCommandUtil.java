package org.example.utils;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuhaifeng
 * @date 2024/06/14/18:58
 */
@Slf4j
public class ExecuteSellCommandUtil {

    /**
     * 读取/proc/stat文件，获取cpu信息的命令
     */
    private static final String CPU_USED_COMMAND = "cat /hostinfo/proc/stat | grep 'cpu '";
    /**
     * 读取/proc/meminfo文件，获取内存的命令
     */
    private static final String MEM_USED_COMMAND = "cat /hostinfo/proc/meminfo | grep Mem";
    /**
     * 读取/etc/hostname文件，获取主机名称
     */
    private static final String ENDPOINT = "cat /hostinfo/proc/hostname";

    /**
     * 获取主机名称
     *
     * @return
     */
    public static String getEndpoint() {
        String[] command = {"/bin/sh", "-c", ENDPOINT};
        String endpoint = RuntimeUtil.execForStr(command);
        return endpoint.trim();
    }

    /**
     * 获取CPU利用率
     *
     * @return
     */
    public static Double getCpuUsed() {
        // -c表示CPU_USED_COMMAND是一整条命令
        String[] command = {"/bin/sh", "-c", CPU_USED_COMMAND};
        // hutool工具包对执行命令的封装
        String s = RuntimeUtil.execForStr(command);
        log.info("获取CPU执行结果:{}", s);
        s = s.substring(5);
        double totalCpu = 0.0;
        double idleCpu = 0.0;
        int index = 0;
        for (String numberStr : s.split(" ")) {
            if (index == 3) {
                idleCpu = Double.parseDouble(numberStr);
            }
            totalCpu += Double.parseDouble(numberStr);
            index++;
        }
        return ((totalCpu - idleCpu) / totalCpu * 100.0);
    }

    /**
     * 获取内存利用率
     *
     * @return
     */
    public static Double getMemUsed() {
        String[] command = {"/bin/sh", "-c", MEM_USED_COMMAND};
        String s = RuntimeUtil.execForStr(command);
        log.info("获取内存执行结果:{}", s);
        double memTotal = 0.0;
        double memAvailable = 0.0;
        s = s.replace(" ", "");
        String[] split = s.split("\n");
        int index = 0;
        for (String string : split) {
            if (index == 0) {
                // MemTotal
                memTotal = Double.parseDouble(string.substring(9, string.indexOf("kB")));
            } else if (index == 2) {
                // MemAvailable
                memAvailable = Double.parseDouble(string.substring(13, string.indexOf("kB")));
            }
            index++;
        }
        return ((memTotal - memAvailable) / memTotal * 100.0);
    }
}
