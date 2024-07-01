import java.io.File;
import java.util.List;

public class DirectoryScanner {

    public static List<File> getUserDirectories(String path, List<File> directories) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在!");
            return directories;
        }

        if (file.getName().contains("不算")) {
            return directories;
        }

        if (file.isDirectory()) {
            if (file.getName().matches("\\d+")) {
                directories.add(file);
            } else {
                scanDirectory(file, directories);
            }
        }

        return directories;
    }

    private static void scanDirectory(File directory, List<File> directories) {
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return;
        }

        for (File file : files) {
            if (file.getName().contains("不算")) {
                continue;
            }

            if (file.isDirectory()) {
                if (file.getName().matches("\\d+")) {
                    directories.add(file);
                } else {
                    getUserDirectories(file.getAbsolutePath(), directories);
                }
            }
        }
    }
}
