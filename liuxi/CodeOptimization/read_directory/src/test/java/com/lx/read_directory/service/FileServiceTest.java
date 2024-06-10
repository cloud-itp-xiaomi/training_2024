package com.lx.read_directory.service;

import com.lx.read_directory.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @Test
    void getUserDirectory() {
       FileService service = new FileServiceImpl();
       List<File> list = new ArrayList<>();
       list = service.getUserDirectory("E:\\java_study\\javaProjects\\UtilizationCollectionSystem\\liuxi\\CodeOptimization\\read_directory\\get-user-directory", list);
       for(File file : list){
           System.out.println(file);
       }
    }
}