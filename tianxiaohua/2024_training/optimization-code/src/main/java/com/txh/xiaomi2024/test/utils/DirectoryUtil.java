package com.txh.xiaomi2024.test.utils;

import java.io.File;

public class DirectoryUtil {
    public static boolean isExcluded(File file, String excludeStr) {
        return file.getName().contains(excludeStr);
    }

    public static boolean isMatched(File file, String matchStr) {
        return file.getName().matches(matchStr);
    }
}
