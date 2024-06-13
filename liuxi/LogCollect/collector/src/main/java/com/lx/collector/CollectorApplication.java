package com.lx.collector;

import com.lx.collector.service.impl.LogServiceImpl;
import com.lx.collector.upload.Collector;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class CollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
//        LogServiceImpl logService = new LogServiceImpl();
//        String file = "E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\java\\com\\lx\\collector\\log\\a.log";
//        Path path = Paths.get(file);
//        logService.monitorLogFile(path);
    }

}
