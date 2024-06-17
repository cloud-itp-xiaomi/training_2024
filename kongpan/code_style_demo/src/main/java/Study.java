import java.io.File;
import java.util.List;

/**
 * @author mrk
 * @create 2024-06-09-20:46
 */
public class Study {

    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }

        if (file.getName().contains("不算")) {
            return list;
        }

        if (file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return list;
        }

        for (File file1 : files) {
            if (file1.getName().contains("不算")) {
                continue;
            }
            if (file1.isDirectory() && !file1.getName().matches("\\d+")) {
                getUserDirectory(file1.getAbsolutePath(), list);
                continue;
            }
            if (file1.isDirectory() && file1.getName().matches("\\d+")) {
                list.add(file1);
            }
        }
        return list;
    }
}
