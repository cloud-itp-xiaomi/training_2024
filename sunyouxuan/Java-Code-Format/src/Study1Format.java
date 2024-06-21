

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum DirectoryFilter {
    EXCLUDE_KEYWORD_1("不算"),
    EXCLUDE_KEYWORD_2("其他关键词1"),
    EXCLUDE_KEYWORD_3("其他关键词2");

    private final String keyword;

    DirectoryFilter(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public static boolean containsExcludeKeyword(String fileName) {
        for (DirectoryFilter filter : DirectoryFilter.values()) {
            if (fileName.contains(filter.getKeyword())) {
                return true;
            }
        }
        return false;
    }
}

public class Study1Format {

    /**
     * 获取指定路径下符合条件的用户目录
     * @param path 目录路径
     * @return 符合条件的目录列表
     */
    public static List<File> getUserDirectories(String path) {
        List<File> list = new ArrayList<>();
        traverseDirectory(new File(path), list);
        return list;
    }

    /**
     * 遍历目录并添加符合条件的目录到列表
     * @param file 当前处理的文件或目录
     * @param list 存放符合条件的目录列表
     */
    private static void traverseDirectory(File file, List<File> list) {
        if (!file.exists()) {
            System.out.println("路径不存在: " + file.getAbsolutePath());
            return;
        }

        // 判断是否需要排除
        if (isExcluded(file)) {
            return;
        }

        if (file.isDirectory()) {
            if (file.getName().matches("\\d+")) {
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File subFile : files) {
                        traverseDirectory(subFile, list);
                    }
                } else {
                    System.out.println("文件夹是空的: " + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 判断文件或目录是否需要排除
     * @param file 当前处理的文件或目录
     * @return 是否需要排除
     */
    private static boolean isExcluded(File file) {
         return DirectoryFilter.containsExcludeKeyword(file.getName()) 
    }

    public static void main(String[] args) {
        // 示例调用
        List<File> result = getUserDirectories("/path/to/directory");
        for (File file : result) {
            System.out.println(file.getAbsolutePath());
        }
    }
}