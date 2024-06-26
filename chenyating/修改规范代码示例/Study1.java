package com.example.demo01.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Study1 {
    public static List<File> getUserDirectory(String path) {
        File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException("路径不存在");
        }

        List<File> result = new ArrayList<>();
        collectFiles(file, result);
        return result;
    }

    private static void collectFiles(File file, List<File> list) {
        if (file.getName().contains("不算")) {
            return;
        }

        if (file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
        }

        File[] files = file.listFiles();

        if (files != null && files.length > 0) {
            for (File file2 : files) {
                collectFiles(file2, list);
            }
        }
    }
}