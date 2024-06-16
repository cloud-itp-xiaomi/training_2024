package com.example.xiaomi1coll.tools;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LogFileListener extends FileAlterationListenerAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(LogFileListener.class);
    private final File file;

    public LogFileListener(File file) {
        this.file = file;
    }

    @Override
    public void onFileChange(File file) {
        if (this.file.equals(file)) {
            logger.info("File changed: {}", file.getAbsolutePath());
            // 文件变更的逻辑
        }
    }
}
