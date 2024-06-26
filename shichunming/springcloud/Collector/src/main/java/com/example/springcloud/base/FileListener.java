package com.example.springcloud.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * @ClassName FileListener
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 17:54
 **/
@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
        log.info("onStart");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info("新建：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryChange(File directory) {
        log.info("修改：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.info("删除：" + directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        log.info("新建：" + compressedPath);
        if (file.canRead()) {
            // TODO 读取或重新加载文件内容
            log.info("文件变更，进行处理");
        }
    }

    @Override
    public void onFileChange(File file) {
        String compressedPath = file.getAbsolutePath();
        log.info("修改：" + compressedPath);
    }

    @Override
    public void onFileDelete(File file) {
        log.info("删除：" + file.getAbsolutePath());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
        log.info("onStop");
    }
}