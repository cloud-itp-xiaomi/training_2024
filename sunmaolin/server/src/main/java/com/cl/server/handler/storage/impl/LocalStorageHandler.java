package com.cl.server.handler.storage.impl;

import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.enums.StorageTypeEnum;
import com.cl.server.handler.storage.StorageTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 本地存储方式
 *
 * @author: tressures
 * @date: 2024/6/5
 */
@Component
@Slf4j
public class LocalStorageHandler implements StorageTypeHandler {

    private static final String PATH = "/data/logs/";

    @Override
    public StorageTypeEnum getHandlerType() {
        return StorageTypeEnum.LOCAL;
    }

    @Override
    public void upload(List<LogInfoDTO> logInfoDTOS) {
        for (LogInfoDTO logInfoDTO : logInfoDTOS) {
            String[] parts = logInfoDTO.getFile().split(File.separator);
            String fileName = parts[parts.length - 1];
            try (FileWriter fileWriter = new FileWriter(PATH + fileName, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                for (String message : logInfoDTO.getLogs()) {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                log.info("LocalStorageHandler.upload.filePath.{}.error:{}", PATH + fileName, e.getMessage());
            }
        }
    }

    @Override
    public LogInfoVO query(LogQueryDTO logQueryDTO) {
        LogInfoVO logInfoVO = new LogInfoVO();
        String[] parts = logQueryDTO.getFile().split(File.separator);
        String fileName = parts[parts.length - 1];
        List<String> logs = new ArrayList<>();
        try (FileReader fileReader = new FileReader(PATH + fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            log.info("LocalStorageHandler.query.filePath.{}.error:{}", PATH + fileName, e.getMessage());
        }
        logInfoVO.setHostname(logQueryDTO.getHostname());
        logInfoVO.setFile(logQueryDTO.getFile());
        logInfoVO.setLogs(logs);
        return logInfoVO;
    }
}
