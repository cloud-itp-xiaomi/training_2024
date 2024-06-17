import java.io.File;
import java.util.List;

public class Study {
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在!");

            return list;
        }
        if (containExcluded(file)) {
            return list;
        }

        if (file.isDirectory()
                && isMatches(file)) {
            list.add(file);
        }
        File[] files = file.listFiles();
        if (files.length == 0) {
            System.out.println("文件夹是空的!");
            
            return list;
        }
        for (File file2 : files) {
            if (containExcluded(file2)) {
                continue;
            }
            if (file2.isDirectory()
                    && !isMatches(file2)) {
                getUserDirectory(file2.getAbsolutePath(), list);
            }
            if (file2.isDirectory()
                    && isMatches(file2)) {
                list.add(file2);
            }
        }

        return list;
    }

    private static boolean containExcluded(File file2) {
        return file2.getName().contains("不算");
    }

    private static boolean isMatches(File file) {
        return file.getName().matches("\\d+");
    }
}