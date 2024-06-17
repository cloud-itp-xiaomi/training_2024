package com.txh.xiaomi2024.test;

import com.txh.xiaomi2024.test.impl.GetUserDirectoryList;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
public class TestApplication {
    @Test
    public void contextLoads() {
        GetUserDirectoryList getUserDirectoryList = new GetUserDirectoryList();
        List<File> userDirectoryList = getUserDirectoryList.getDirectoryList("/home/txh/users", "不算", "\\d+");
        for (File file : userDirectoryList) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
