import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<File> getUserDirectory(String path, List< File > list) {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }

        if(file.getName().contains("不算")) {
            System.out.println("非生产文件!");
            return list;
        }

        if(file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if(files.length == 0) {
            System.out.println("文件夹是空的!");
            return list;
        }
        for(File file2: files) {
            if(file2.getName().contains("不算")) {
                continue;
            }

            if(file2.isDirectory() && !file2.getName().matches("\\d+")) {
                getUserDirectory(file2.getAbsolutePath(), list);
                continue;
            }

            if (file2.isDirectory() && file2.getName().matches("\\d+")) {
                list.add(file2);
            }
        }


        return list;
    }

    public static void main(String[] args) {
        List<File> userDirectories = new ArrayList<>();
        String path = "D:\\1";
        getUserDirectory(path, userDirectories);
        for(File directory : userDirectories) {
            System.out.println(directory.getAbsolutePath());
        }
    }
}
