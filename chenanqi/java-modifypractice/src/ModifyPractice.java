import java.io.File;
import java.util.List;

public class ModifyPractice {
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在");
            return list;
        }

        if (file.getName().contains("不算")) {
            return list;
        }

        File[] files = file.listFiles();
        if (file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }

        for (File file2 : files) {
            if (file2.getName().contains("不算")) {
                continue;
            }
            if (file2.isDirectory() && !file2.getName().matches("\\d+")) {
                getUserDirectory(file2.getAbsolutePath(), list);
                continue;
            }
            if (file2.isDirectory() && file2.getName().matches("\\d+")) {
                list.add(file2);
            }
        }
        return list;
    }
}

