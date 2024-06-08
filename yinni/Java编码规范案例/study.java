public class Study1 {

    private static final String EXCLUDED_NAME = "不算";
    private static final String EMPTY_MESSAGE = "文件夹是空的!";
    private static final String NOT_FOUND_MESSAGE = "文件不存在!";
    private static final String NUMERIC_REGEX = "\\d+";

    public static boolean isNumericFolder(File file) {
        return file.isDirectory() && file.getName().matches(NUMERIC_REGEX);
    }

    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println(NOT_FOUND_MESSAGE);
            return list;
        }

        if (file.getName().contains(EXCLUDED_NAME)) {
            return list;
        }

        if (isNumericFolder(file)) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println(EMPTY_MESSAGE);
            return list;
        }

        for (File file2 : files) {
            if (file2.getName().contains(EXCLUDED_NAME)) {
                continue;
            }
            if (file2.isDirectory()) {
                if (isNumericFolder(file2)) {
                    list.add(file2);
                } else {
                    getUserDirectory(file2.getAbsolutePath(), list);
                }
            }
        }

        return list;
    }
