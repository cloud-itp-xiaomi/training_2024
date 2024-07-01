package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.LogSaveTypeEnum;
import org.example.exception.BaseException;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 本地文件日志保存处理器
 *
 * @author liuhaifeng
 * @date 2024/06/15/1:09
 */
@Slf4j
@Component
@RefreshScope
public class LocalFileLogSaveHandle implements AbstractLogSaveHandler {

    @Value("${utilization-server.local.file-path}")
    private String localFilePath;

    @PostConstruct
    public void init() {
        LogSaveHandlerFactory.register(LogSaveTypeEnum.LOCAL_FILE, this);
    }

    @Override
    public void save(List<LogUploadDTO> logUploadDTOList) {
        for (LogUploadDTO logUploadDTO : logUploadDTOList) {
            for (String logContent : logUploadDTO.getLogs()) {
                String str = logUploadDTO.getHostname() + " " + logUploadDTO.getFile() + "\n" + logContent + "\n";
                log.info("日志保存到：{}", localFilePath);
                File file = new File(localFilePath);
                try {
                    if (!file.exists()) {
                        boolean b = file.createNewFile();
                        if (!b) {
                            throw new BaseException("本地日志存储文件创建失败");
                        }
                    }
                    OutputStream outputStream = new FileOutputStream(file, true);
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(str);
                    outputStream.close();
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BaseException("本地文件保存失败");
                }
            }
        }
    }

    @Override
    public LogQueryVO query(LogQueryDTO logQueryDTO) {
        List<String> logs = new ArrayList<>();
        try (
                Reader reader = new FileReader(localFilePath);
                BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(logQueryDTO.getHostname()) && line.contains(logQueryDTO.getFile())) {
                    String[] split = line.split(" ");
                    if (Objects.equals(split[0], logQueryDTO.getHostname()) && Objects.equals(split[1], logQueryDTO.getFile())) {
                        logs.add(bufferedReader.readLine());
                    }
                }
            }
        } catch (IOException e) {
            throw new BaseException("查询日志文件出错");
        }
        if (logs.isEmpty()) {
            throw new BaseException("没有查询到日志");
        }
        LogQueryVO logQueryVO = new LogQueryVO();
        logQueryVO.setHostname(logQueryDTO.getHostname());
        logQueryVO.setFile(logQueryDTO.getFile());
        logQueryVO.setLogs(logs);
        return logQueryVO;
    }
}
