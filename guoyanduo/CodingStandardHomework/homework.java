public class Study1 {

    public static List<File> getUserDirectories(String path, List<File> list) {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("文件不存在！");
            return list;
        }

        if (file.isDirectory()) {
            if (file.getName().matches("\\d+")) {
                list.add(file);
            }

            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("文件夹是空的");
                return list;
            }

            for (File file2 : files) {
                if (file2.getName().contains("不算")) {
                    continue;
                }
                if (file2.isDirectory()) {
                    getUserDirectories(file2.getAbsolutePath(), list);
                }
            }
        }
        return list;
    }
}
