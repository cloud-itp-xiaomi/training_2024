package com.txh.xiaomi2024.work.collector.util;

public class PathUtil {
    /**
     * 连接root和levels生成文件路径
     *
     * @param root   root 根目录
     * @param levels levels 子目录顺序列表
     * @return 路径
     */
    public static String concat(String root, String... levels) {
        String separator = "/";
        StringBuilder path = new StringBuilder(root);
        for (String level : levels) {
            // 移除level前后“/”
            if (level.startsWith(separator)) {
                level = level.substring(1);
            }
            if (level.endsWith(separator)) {
                level = level.substring(0, level.length() - 1);
            }
            // 前面path末尾有“/”
            if (path.lastIndexOf(separator) == path.length() - 1) {
                path.append(level);
            } else {
                path.append(separator).append(level);
            }
        }
        return path.toString();
    }
}
