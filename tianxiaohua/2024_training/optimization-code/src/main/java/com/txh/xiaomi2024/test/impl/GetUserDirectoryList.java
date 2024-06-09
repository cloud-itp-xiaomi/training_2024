package com.txh.xiaomi2024.test.impl;

import com.txh.xiaomi2024.test.GetDirectoryList;
import com.txh.xiaomi2024.test.utils.DirectoryUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetUserDirectoryList implements GetDirectoryList {
    @Override
    public List<File> getDirectoryList(String path, String excludeStr, String matchStr) {
        List<File> list = new ArrayList<>();
        traverseDirectory(new File(path), list, excludeStr, matchStr);
        return list;
    }

    private static void traverseDirectory(File file, List<File> list, String excludeStr, String matchStr) {
        if (!file.exists()) {
            System.out.println("路径不存在: " + file.getAbsolutePath());
            return;
        }

        if (DirectoryUtil.isExcluded(file, excludeStr)) {  // 写了一个配置类
            return;
        }

        if (file.isDirectory()) {
            if (DirectoryUtil.isMatched(file, matchStr)) { // 同样在配置类
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File subFile : files) {
                        traverseDirectory(subFile, list, excludeStr, matchStr);
                    }
                }
            }
        }
    }
}
