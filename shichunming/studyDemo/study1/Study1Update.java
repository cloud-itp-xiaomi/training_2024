package study1;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @ClassName Study1Update
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-07 19:04
 **/
public class Study1Update {
    private static final String NUMERICAL_PATTERN = "\\d+";
    private static final String EXCLUDE_DIR_SUBSTRING = "不算";

    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在");
            return list;
        }
        if (file.getName().contains(EXCLUDE_DIR_SUBSTRING)) {
            return list;
        }
        if (file.isDirectory() && file.getName().contains(NUMERICAL_PATTERN)) {
            list.add(file);
            return list;
        }
        File[] files = file.listFiles();
        if (files.length == 0) {
            System.out.println("文件夹是空的!");
        } else {
            for (File file2 : files) {
                getUserDirectory(file2.getAbsolutePath(), list);
            }
        }
        return list;
    }


    /*
     * 第二种方法
     */
    public class Study1Update2 {
        private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
        private static final String EXCLUDE_DIR_SUBSTRING = "不算";

        public static List<File> getUserDirectory(String path, List<File> list) {
            File file = new File(path);
            validateFile(file, list);
            return list;
        }

        private static void validateFile(File file, List<File> list) {
            if (!file.exists()) {
                System.out.println("文件不存在");
                return;
            }
            if (file.getName().contains(EXCLUDE_DIR_SUBSTRING)) {
                return;
            }
            if (file.isDirectory()) {
                handleDirectory(file, list);
            }
        }

        private static void handleDirectory(File dir, List<File> list) {
            if (NUMERIC_PATTERN.matcher(dir.getName()).matches()) {
                list.add(dir);
            }
            File[] files = dir.listFiles();
            if (files != null) {
                if (Stream.of(files).noneMatch(File::isDirectory)) {
                    System.out.println("文件夹是空的");
                    return;
                }
                for (File child : files) {
                    validateFile(child, list);
                }
            }
        }
    }
}