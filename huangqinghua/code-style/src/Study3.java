import java.io.File;
import java.util.List;

public class Study3 {

    /**
     * 获取用户目录，递归查找符合条件的目录
     *
     * @param path 起始路径
     * @param list 存储符合条件目录的列表
     * @return 更新后的目录列表
     */
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }

        if (shouldSkip(file)) {
            return list;
        }

        processDirectory(file, list);

        return list;
    }

    /**
     * 判断是否跳过该文件或目录
     *
     * @param file 要检查的文件或目录
     * @return 如果需要跳过返回true，否则返回false
     */
    private static boolean shouldSkip(File file) {
        return file.getName().contains("不算") || !file.isDirectory();
    }

    /**
     * 处理目录，递归查找子目录并添加符合条件的目录到列表中
     *
     * @param file 当前处理的目录
     * @param list 存储符合条件目录的列表
     */
    private static void processDirectory(File file, List<File> list) {
        // 如果目录名是纯数字，则添加到列表中
        if (file.getName().matches("\\d+")) {
            list.add(file);
            return;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return;
        }

        // 遍历子文件和子目录
        for (File file2 : files) {
            if (shouldSkip(file2)) {
                continue;
            }
            processDirectory(file2, list);
        }
    }
}
