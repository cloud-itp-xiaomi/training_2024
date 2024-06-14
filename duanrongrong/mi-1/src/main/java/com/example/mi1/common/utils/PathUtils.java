package com.example.mi1.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author uchin/李玉勤
 * @date 2023/3/11 16:12
 * @description
 */
public class PathUtils {
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

    /**
     * 按指定类型转换路径分隔符
     */
    public enum PathConvert {
        UnixPath {
            @Override
            public String convert(String winPath) {
                return winPath.replace("\\", "/");
            }
        },
        WinPath {
            @Override
            public String convert(String unixPath) {
                return unixPath.replace("/", "\\");
            }
        },
        SysPath {
            @Override
            public String convert(String filePath) {
                String sysName = System.getProperties().getProperty("os.name");
                if (Pattern.matches(".*Win.*", sysName)) {
                    return filePath.replace("/", "\\");
                } else {
                    return filePath.replace("\\", "/");
                }

            }
        };

        public abstract String convert(String path);
    }

    /**
     * 判断是否是 windows 的路径
     * todo
     *
     * @param input 输入字符串
     * @return 是否是路径
     */
    public static boolean isValidFilePath(String input) {
        // 定义正则表达式模式
        String windowsPattern = "^[a-zA-Z]:\\\\.+";
        String unixPattern = "^/.*";

        // 编译正则表达式模式
        Pattern windowsPatternRegex = Pattern.compile(windowsPattern);
        Pattern unixPatternRegex = Pattern.compile(unixPattern);

        // 使用 Matcher 执行匹配
        Matcher windowsMatcher = windowsPatternRegex.matcher(input);
        Matcher unixMatcher = unixPatternRegex.matcher(input);

        // 返回匹配结果
        return windowsMatcher.matches() || unixMatcher.matches();
    }

    public static boolean isWinPath(String input) {
        // 定义正则表达式模式
        String windowsPattern = "^[a-zA-Z]:\\\\.+";

        // 编译正则表达式模式
        Pattern windowsPatternRegex = Pattern.compile(windowsPattern);

        // 使用 Matcher 执行匹配
        Matcher windowsMatcher = windowsPatternRegex.matcher(input);

        // 返回匹配结果
        return windowsMatcher.matches();
    }

    public static void main(String[] args) {
        String path1 = "C:\\path\\to\\file.txt";
        String path2 = "/usr/local/bin/script.sh";
        String path3 = "D:/path/to/file.txt";
        String path4 = "invalid/path";

        System.out.println("Is path1 a valid file path? " + isValidFilePath(path1));// true
        System.out.println("Is path2 a valid file path? " + isValidFilePath(path2));// true
        System.out.println("Is path3 a valid file path? " + isValidFilePath(path3));// false
        System.out.println("Is path4 a valid file path? " + isValidFilePath(path4));// false
    }
}
