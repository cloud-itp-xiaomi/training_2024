package org.example.service.impl;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.UtilizationClient;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/06/02/14:57
 */
@Slf4j
@Service
public class CollectorServiceImpl implements CollectorService {

    @Autowired
    private UtilizationClient utilizationClient;

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

    @Override
    public void upload() {
        String endpoint = getEndpoint();
        log.info("采集到主机名称:{}", endpoint);
        Double cpuUsed = getCpuUsed();
        log.info("采集到CPU利用率:{}", cpuUsed);
        Double memUsed = getMemUsed();
        log.info("采集到内存利用率:{}", memUsed);
        UtilizationUploadDTO cpuUploadDTO = new UtilizationUploadDTO();
        cpuUploadDTO.setEndpoint(endpoint);
        cpuUploadDTO.setMetric("cpu.used.percent");
        cpuUploadDTO.setValue(cpuUsed);
        cpuUploadDTO.setTimestamp(System.currentTimeMillis() / 1000);
        cpuUploadDTO.setStep(60);
        UtilizationUploadDTO memUploadDTO = new UtilizationUploadDTO();
        memUploadDTO.setEndpoint(endpoint);
        memUploadDTO.setMetric("mem.used.percent");
        memUploadDTO.setValue(memUsed);
        memUploadDTO.setTimestamp(System.currentTimeMillis() / 1000);
        memUploadDTO.setStep(60);
        List<UtilizationUploadDTO> utilizationUploadDTOList = new ArrayList<>();
        utilizationUploadDTOList.add(cpuUploadDTO);
        utilizationUploadDTOList.add(memUploadDTO);
        Result<Void> upload = utilizationClient.upload(utilizationUploadDTOList);
        if (upload.getCode() != 1) {
            log.error("上传失败");
        }
    }

    /**
     * 获取主机名称
     *
     * @return
     */
    private static String getEndpoint() {
        String[] command = {"/bin/sh", "-c", ENDPOINT};
        String endpoint = RuntimeUtil.execForStr(command);
        return endpoint;
    }

    /**
     * 获取CPU利用率
     *
     * @return
     */
    private static Double getCpuUsed() {
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
    private static Double getMemUsed() {
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
