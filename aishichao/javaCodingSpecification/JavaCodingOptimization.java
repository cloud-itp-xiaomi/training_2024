public class Study {
    public static List<File> getUserDirectory(String path, List < File > list) {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }
        String fileName = file.getName();

        if(fileName.contains("不算")) {
            System.out.println("非生产文件!");
            return list;
        }

        if(file.isDirectory() && fileName.matches("\\d+")) {
            list.add(file);
            return list;
        }

        File[] files = file.listFiles();
        if(files.length == 0) {
            System.out.println("文件夹是空的!");
            return list;
        }
        for(File subFile: files) {
            String subFileName = subFile.getName();
            if(subFileName.contains("不算")) {
                    continue;
            }

            if(subFile.isDirectory() && !subFileName.matches("\\d+")) {
                getUserDirectory(subFile.getAbsolutePath(), list);
                continue;
            }

            if (subFile.isDirectory() && subFileName.matches("\\d+")) {
                list.add(subFile);
            }
        }


        return list;
    }
}
