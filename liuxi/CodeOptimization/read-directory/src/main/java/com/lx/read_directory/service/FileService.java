package com.lx.read_directory.service;

import java.io.File;
import java.util.List;

public interface FileService {

    /**
     * 读取指定路径下的全部文件并返回
     * @param path 绝对路径
     */
    public List<File> getUserDirectory(String path , List<File> list);

}
