import java.io.File;
import java.util.List;

public class Study1 {
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }

        processDirectory(file, list);
        return list;
    }

    private static void processDirectory(File directory, List<File> list) {
        if (shouldIgnore(directory)) {
            return;
        }

        if (isNumericDirectory(directory)) {
            list.add(directory);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return;
        }

        for (File file : files) {
            processFile(file, list);
        }
    }

    private static void processFile(File file, List<File> list) {
        if (shouldIgnore(file) || !file.isDirectory()) {
            return;
        }

        if (isNumericDirectory(file)) {
            list.add(file);
        } else {
            processDirectory(file, list);
        }
    }

    private static boolean shouldIgnore(File file) {
        return file.getName().contains("不算");
    }

    private static boolean isNumericDirectory(File file) {
        return file.isDirectory() && file.getName().matches("\\d+");
    }
}
