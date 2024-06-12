package com.lx.read_directory.service;

import com.lx.read_directory.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class FileServiceTest {

    @Test
    void getUserDirectory() {
       FileService service = new FileServiceImpl();
       List<File> list = new ArrayList<>();
       list = service.getUserDirectory("E:\\java_study\\javaProjects\\UtilizationCollectionSystem\\liuxi\\CodeOptimization\\read-directory\\src\\test\\java\\com\\lx\\read_directory\\service\\get-user-directory", list);
       for(File file : list){
           System.out.println(file);
       }
    }
}