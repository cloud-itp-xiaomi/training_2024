import java.io.File;
import java.util.List;

public class Study1 {

    /**
     * 递归地获取指定路径下符合条件的目录，并将其添加到列表中。
     *
     * @param path 目录或文件的路径
     * @param list 存储符合条件的目录的列表
     * @return 符合条件的目录列表
     */
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (file.exists()) {
            if (file.getName().contains("不算")) {
                return list;
            }
            if (file.isDirectory() && file.getName().matches("\\d+")) {
                list.add(file);
            } else {
                // 递归处理子目录和文件
                File[] files = file.listFiles();
                if (files != null && files.length > 0) { // 确保files不为null
                    for (File file2 : files) {
                        if (file2.getName().contains("不算")) {
                            continue;
                        }
                        getUserDirectory(file2.getAbsolutePath(), list);
                    }
                } else {
                    System.out.println("文件夹是空的!");
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return list;
    }

}