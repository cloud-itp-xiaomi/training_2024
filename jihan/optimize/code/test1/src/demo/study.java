package demo;

public class study {

    public static List<File> getUserDirectories(String path) {
        List<File> list = new ArrayList<>();
        traverseDirectory(new File(path), list);
        return list;
    }

    private static void traverseDirectory(File file, List<File> list) {
        if (!file.exists()) {
            System.out.println("路径不存在: " + file.getAbsolutePath());
            return;
        }

        if (isExcluded(file)) { // 新增方法，判断是否需要排除
            return;
        }

        if (file.isDirectory()) {
            if (file.getName().matches("\\d+")) {
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File subFile : files) {
                        traverseDirectory(subFile, list);
                    }
                }
            }
        }
    }

    private static boolean isExcluded(File file) {
        return file.getName().contains("不算") ;
    }
}