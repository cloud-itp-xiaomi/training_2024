import java.io.File;
import java.util.List;

public class Study2 {
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }
        if (file.getName().contains("不算")) {
            return list;
        }
        if (!file.isDirectory()) {
            return list;
        }
        if (file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return list;
        }
        for (File file2 : files) {
            if (file2.getName().contains("不算")) {
                continue;
            }
            if (!file2.isDirectory()) {
                continue;
            }
            if (file2.getName().matches("\\d+")) {
                list.add(file2);
            } else {
                getUserDirectory(file2.getAbsolutePath(), list);
            }
        }

        return list;
    }
}