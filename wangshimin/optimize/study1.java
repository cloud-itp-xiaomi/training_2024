public class Study1 {
    private static final String INVALID_NAME = "不算";
    private static final String DIRECTORY_PATTERN = "\\d+";

    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }

        if (file.getName().contains(INVALID_NAME)) {
            return list;
        }

        if (file.isDirectory()) {
            processDirectory(file, list);
        } else {
            System.out.println("不是一个文件夹!");
        }

        return list;
    }

    private static void processDirectory(File directory, List<File> list) {
        if (directory.getName().matches(DIRECTORY_PATTERN)) {
            list.add(directory);
        } else {
            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    getUserDirectory(file.getAbsolutePath(), list);
                }
            }
        }
    }
}
