import java.io.File;
import java.util.List;
public class Study1 {
    public static List< File > getUserDirectory(String path, List < File > list) {
        File file = new File(path);
        if(file.exists()) {
            if(file.getName().contains("不算")) {
                return list;
            }
            if(file.isDirectory() && file.getName().matches("\\d+")) {
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if(files.length == 0) {
                    System.out.println("文件夹是空的!");
                } else {
                    for(File file2: files) {
                        if(file2.getName().contains("不算")) {
                            continue;
                        } else if(file2.isDirectory() && !file2.getName().matches("\\d+")) {
                            getUserDirectory(file2.getAbsolutePath(), list);
                        } else if(file2.isDirectory() && file2.getName().matches("\\d+")) {
                            list.add(file2);
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return list;
    }
}