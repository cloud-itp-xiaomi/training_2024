package com.txh.xiaomi2024.work.service.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PathUtil {
    /**
     * 连接root和levels生成文件路径
     *
     * @param root   root 根目录
     * @param levels levels 子目录顺序列表
     * @return 路径
     */
    public static String concat(String root,
                                String... levels) {
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

    /**
     * 判断是否是有效的路径
     *
     * @param input 输入字符串
     * @return 是否是路径
     */
    public static boolean isValidFilePath(String input) {
        // 定义正则表达式模式
        String unixPattern = "^/.*";

        // 编译正则表达式模式
        Pattern unixPatternRegex = Pattern.compile(unixPattern);

        // 使用 Matcher 执行匹配
        Matcher unixMatcher = unixPatternRegex.matcher(input);

        // 返回匹配结果
        return unixMatcher.matches();
    }
}
