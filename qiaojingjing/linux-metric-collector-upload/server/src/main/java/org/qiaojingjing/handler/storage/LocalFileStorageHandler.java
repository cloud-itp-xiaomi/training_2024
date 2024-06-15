package org.qiaojingjing.handler.storage;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.config.LocalFileParentPath;
import org.qiaojingjing.constant.ExceptionConstant;
import org.qiaojingjing.exception.CreateFileAndDirFailException;
import org.qiaojingjing.exception.MyFileNotFoundException;
import org.qiaojingjing.exception.MyIOException;
import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.vo.LogVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 存储方式：本地文件
 * 根据传过来的文件路径绝对路径和配置文件的父路径创建文件夹
 * 并且将日志内容存储到对应的文件
 *
 * @author qiaojingjing
 * @version 0.1.0
 * @since 0.1.0
 **/

@Service
@Slf4j
public class LocalFileStorageHandler implements StorageTypeHandler {
    @Resource
    private LocalFileParentPath localFileParentPath;

    @Override
    public void save(List<LogDTO> logDTOList) {
        log.info("使用本地文件进行存储");
        for (LogDTO logDTO : logDTOList) {
            StringBuilder filePath = new StringBuilder();
            filePath.append("/")
                    .append(logDTO.getHostname())
                    .append(logDTO.getFile());
            String filePathString = filePath.toString();
            String[] logs = logDTO.getLogs();
            String parentPath = localFileParentPath.getParentPath();

            saveInFile(filePathString,
                       parentPath,
                       logs);
        }
    }

    @Override
    public List<LogVO> query(HostDTO hostDTO) {
        log.info("在本地文件查询日志");
        List<LogVO> logList = new ArrayList<>();
        String parentPath = localFileParentPath.getParentPath();
        String childPath = "/" + hostDTO.getHostname() + hostDTO.getFile();
        String absolutePath = parentPath + childPath;

        try {
            FileReader fileReader = new FileReader(absolutePath);
            BufferedReader readLog = new BufferedReader(fileReader);
            String log;
            List<String> logs = new ArrayList<>();
            while ((log = readLog.readLine()) != null){
                logs.add(log);
            }
            LogVO logVO = LogVO.builder()
                               .hostname(hostDTO.getHostname())
                               .file(hostDTO.getFile())
                               .logs(logs)
                               .build();
            logList.add(logVO);
        } catch (FileNotFoundException e) {
            log.error("{}文件不存在!",absolutePath);
            throw new MyFileNotFoundException(ExceptionConstant.FILE_NOT_FOUND);
        } catch (IOException e) {
            log.error("读取错误!{}",e.toString());
            throw new MyIOException(ExceptionConstant.WRITE_FAIL);
        }

        return logList;
    }

    private void saveInFile(String childPath,
                            String parentPath,
                            String[] logs) {
        String[] pathParts = childPath.split("/");
        String fileName = pathParts[pathParts.length - 1];
        String sonPath = getSonPath(pathParts);

        String newFilePath = new File(parentPath, sonPath).getPath();
        File newFile = new File(newFilePath,
                                fileName);
        File newDir = newFile.getParentFile();
        if (!newDir.exists()) {
            boolean mkdirsSuccess = newDir.mkdirs();
            if (!mkdirsSuccess) {
                log.error("创建目录失败: {}", newDir.getAbsolutePath());
                throw new CreateFileAndDirFailException(ExceptionConstant.CREATE_FAIL);
            }
        }
        try {
            if (!newFile.exists()) {
                boolean newFileCreated = newFile.createNewFile();
                if (!newFileCreated) {
                    log.error("创建文件失败: {}", newFile.getAbsolutePath());
                    throw new CreateFileAndDirFailException(ExceptionConstant.CREATE_FAIL);
                }
            }
            FileWriter fileWriter = new FileWriter(newFile, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (String aLog : logs) {
                writer.write(aLog);
                writer.newLine();
            }
            writer.close();
            fileWriter.close();
            log.info("日志存储于：{} ", newFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("写入文件发生错误!:{}", e.toString());
            throw new MyIOException(ExceptionConstant.WRITE_FAIL);
        }
    }
    private String getSonPath(String[] pathParts) {
        boolean hasSonDir = pathParts.length > 1;
        String sonPath = "";
        if (hasSonDir) {
            String[] subDirParts = Arrays.copyOfRange(pathParts, 0, pathParts.length - 1);
            sonPath = String.join(File.separator,
                                  subDirParts);
        }

        return sonPath;
    }
}
