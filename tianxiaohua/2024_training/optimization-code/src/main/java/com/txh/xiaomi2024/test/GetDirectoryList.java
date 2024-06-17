package com.txh.xiaomi2024.test;

import java.io.File;
import java.util.List;

public interface GetDirectoryList {
    List<File> getDirectoryList(String path, String excludeStr, String matchStr);
}
