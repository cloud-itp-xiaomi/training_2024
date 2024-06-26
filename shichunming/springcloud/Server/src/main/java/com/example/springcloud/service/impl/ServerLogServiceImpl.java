package com.example.springcloud.service.impl;

import com.example.springcloud.base.MyException;
import com.example.springcloud.base.SnowflakeIdGenerator;
import com.example.springcloud.base.enums.ErrorCode;
import com.example.springcloud.controller.request.LogQueryRequest;
import com.example.springcloud.controller.request.LogUploadRequest;
import com.example.springcloud.controller.response.LogQueryResponse;
import com.example.springcloud.mapper.XmLogMapper;
import com.example.springcloud.po.LogPo;
import com.example.springcloud.service.ServerLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ServerLogServiceImpl
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 00:01
 **/
@Slf4j
@Service
public class ServerLogServiceImpl implements ServerLogService {
    @Value("${app.log_storage}")
    private String logStorage;
    @Resource
    private XmLogMapper xmLogMapper;
    private static final String BASEDIR = "./localFile/";
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Override
    public void logUpload(LogUploadRequest request) {
        try {
            if ("mysql".equalsIgnoreCase(logStorage)) {
                log.info("保存到mysql");
                saveToDatabase(request);
            } else if ("local_file".equalsIgnoreCase(logStorage)) {
                log.info("保存到本地文件");
                saveToFile(request);
            } else {
                log.info("默认保存到本地文件");
                saveToFile(request);
            }
        } catch (Exception e) {
            log.error("An error occurred during log upload: ", e);
        }
    }

    public void saveToFile(LogUploadRequest request) {

        Path path = createPath(request.getHostName(), request.getFile());
        try {

            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            // 检查文件是否存在
            if (Files.exists(path)) {
                // 文件存在，追加写入
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                    for (String log : request.getLogs()) {
                        writer.write(log);
                        writer.newLine();
                    }
                }
            } else {
                // 文件不存在，创建新文件并写入
                Files.createFile(path);
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    for (String log : request.getLogs()) {
                        writer.write(log);
                        writer.newLine();
                    }
                }
            }
            log.info("日志成功保存至文件: {}", path);
        } catch (AccessDeniedException ae) {
            log.info("没有权限写入文件或创建目录: ", ae);
        } catch (NoSuchFileException nsfe) {
            log.info("目录不存在或路径错误: ", nsfe);
        } catch (IOException e) {
            log.error("写入文件时发生其他错误: ", e);
        }
    }

    public void saveToDatabase(LogUploadRequest request) {
        List<LogPo> logPosToSave = request.getLogs().stream()
                .map(log -> {
                    LogPo logPo = new LogPo();
                    logPo.setId(snowflakeIdGenerator.nextId());
                    logPo.setHostname(request.getHostName());
                    logPo.setFile(request.getFile());
                    logPo.setLog(log);
                    logPo.setIsDelete(0);
                    logPo.setCreateTime(LocalDateTime.now());
                    logPo.setUpdateTime(LocalDateTime.now());
                    return logPo;
                })
                .collect(Collectors.toList());
        xmLogMapper.insertBatch(logPosToSave);
    }

    @Override
    public LogQueryResponse queryLog(LogQueryRequest request) {
        log.info("请求文件路径：{}",request.getFile());
        if ("mysql".equalsIgnoreCase(logStorage)) {
            log.info("从mysql查询日志");
            return queryFromDatabase(request);
        } else if ("local_file".equalsIgnoreCase(logStorage)) {
            log.info("从本地文件查询日志");
            return queryFromFile(request);
        } else {
            log.info("默认从本地文件查询日志");
            return queryFromFile(request);
        }
    }

    public LogQueryResponse queryFromDatabase(LogQueryRequest request) {
        LogQueryResponse response = new LogQueryResponse();
        List<LogPo> logPos = xmLogMapper.selectByFileAndHostname(request.getFile(), request.getHostName());
        response.setHostName(request.getHostName());
        response.setFile(request.getFile());
        response.setLogs(logPos.stream().map(LogPo::getLog).collect(Collectors.toList()));
        return response;
    }

    public LogQueryResponse queryFromFile(LogQueryRequest request) {
        File filePath = createFile(request.getHostName(), request.getFile());
        List<String> allLogs = new ArrayList<>();
        log.info("请求文件路径: {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLogs.add(line);
            }
        } catch (IOException e) {
            log.error("从文件读取日志时发生错误: ", e);
            throw new MyException(ErrorCode.FILE_EXCEPTION, "读取日志错误");
        }

        LogQueryResponse response = new LogQueryResponse();
        response.setHostName(request.getHostName());
        response.setFile(String.valueOf(filePath));
        response.setLogs(allLogs);

        return response;
    }

    private File createFile(String hostName, String file) {
        String filePath = BASEDIR + hostName + file;
        File path = new File(filePath);
        return path;
    }

    private Path createPath(String hostName, String fileName) {
        String filePath = BASEDIR + hostName + fileName;
        Path path = Paths.get(filePath);
        return path;
    }

}
