package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.example.listener.FileChangeListener;
import org.example.service.LogCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 文件监听工厂
 *
 * @author liuhaifeng
 * @date 2024/06/14/1:42
 */
@Slf4j
@Component
public class FileListenerFactory {

    @Autowired
    private LogCollectorService logCollectorService;

    // 设置轮询间隔（毫秒）
    private final long interval = 1000;

    public FileAlterationMonitor getMonitor(String filePath) throws IOException {
        String directoryPath = filePath.substring(0, filePath.lastIndexOf("/"));
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        // 创建一个文件过滤器，只监听对应的文件
        IOFileFilter fileFilter = FileFilterUtils.nameFileFilter(fileName);
        IOFileFilter dirFilter = FileFilterUtils.and(FileFilterUtils.directoryFileFilter());
        IOFileFilter filter = FileFilterUtils.or(fileFilter, dirFilter);
        FileAlterationObserver observer = new FileAlterationObserver(new File(directoryPath), filter);
        // 向监听者添加监听器
        observer.addListener(new FileChangeListener(filePath, logCollectorService));
        // 返回监听者
        return new FileAlterationMonitor(interval, observer);
    }
}
