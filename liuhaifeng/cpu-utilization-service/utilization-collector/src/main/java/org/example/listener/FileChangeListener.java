package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.example.exception.BaseException;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.service.LogCollectorService;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件监听器
 *
 * @author liuhaifeng
 * @date 2024/06/14/0:47
 */
@Slf4j
public class FileChangeListener extends FileAlterationListenerAdaptor {

    private final RandomAccessFile randomAccessFile;
    private LogCollectorService logCollectorService;

    public FileChangeListener(String filePath, LogCollectorService logCollectorService) throws IOException {
        this.randomAccessFile = new RandomAccessFile(filePath, "r");
        // 将指针移动到文件末尾
        randomAccessFile.seek(randomAccessFile.length());
        this.logCollectorService = logCollectorService;
    }

    /**
     * 日志上传
     *
     * @param log
     */
    private void uploadLog(String filepath, List<String> log) {
        List<LogUploadDTO> list = new ArrayList<>();
        LogUploadDTO logUploadDTO = new LogUploadDTO();
        logUploadDTO.setFile(filepath);
        logUploadDTO.setLogs(log);
        list.add(logUploadDTO);
        logCollectorService.upload(list);
    }

    @Override
    public void onFileChange(File file) {
        log.info("修改文件：{}", file.getAbsolutePath());
        // 读取新增内容
        List<String> strList = new ArrayList<>();
        try {
            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                String str = new String(randomAccessFile.readLine().getBytes("ISO-8859-1"), "UTF-8");
                // 过滤掉行尾的空字符
                if (!StringUtils.isEmpty(str)) {
                    strList.add(str);
                }
            }
            log.info("新增日志内容：{}", strList);
            // 文件改变时，重新定位到文件末尾
            randomAccessFile.seek(randomAccessFile.length());
            uploadLog(file.getAbsolutePath(), strList);
        } catch (IOException e) {
            throw new BaseException("读取日志文件失败");
        }
    }
}
