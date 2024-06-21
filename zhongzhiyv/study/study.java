public class Study1 {

    /**
     * @param path the starting directory path
     * @param directories the list to collect matching directories
     * @return the list of matching directories
     */
    public static List<File> getUserDirectory(String path, List<File> directories) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在!");
            return directories;
        }

        if (isExcluded(file)) {
            return directories;
        }

        if (file.isDirectory() && file.getName().matches("\\d+")) {
            directories.add(file);
            return directories;
        }

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件夹是空的!");
            return directories;
        }

        for (File subFile : files) {
            if (isExcluded(subFile)) {
                continue;
            }

            if (subFile.isDirectory()) {
                if (subFile.getName().matches("\\d+")) {
                    directories.add(subFile);
                } else {
                    getUserDirectory(subFile.getAbsolutePath(), directories);
                }
            }
        }

        return directories;
    }

    private static boolean isExcluded(File file) {
        return file.getName().contains("不算") ;
    }

}