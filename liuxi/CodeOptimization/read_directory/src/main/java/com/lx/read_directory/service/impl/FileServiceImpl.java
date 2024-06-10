package com.lx.read_directory.service.impl;

import com.lx.read_directory.service.FileService;

import java.io.File;
import java.util.List;

public class FileServiceImpl implements FileService {

    @Override
    public List<File> getUserDirectory(String path, List<File> list) {

        File file = new File(path);

        if(!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }
        if(file.getName().contains("不算")) {
            return list;
        }
        if(!file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }
        File[] files = file.listFiles();
        if(files != null && files.length == 0) {
            System.out.println("文件夹是空的!");
        }else if(files != null){
            for (File file1 : files) {
                getUserDirectory(file1.getAbsolutePath(), list);
            }
        }
        return list;
    }
}
