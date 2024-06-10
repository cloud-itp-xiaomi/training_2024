public class Study1 {
    public static List< File > getUserDirectory(String path, List < File > list) {
        // File file = new File(path);
        // if(file.exists()) {
            // if(file.getName().contains("����")) {
                // return list;
            // }
            // if(file.isDirectory() && file.getName().matches("\\d+")) {
                // list.add(file);
            // } else {
                // File[] files = file.listFiles();
                // if(files.length == 0) {
                    // System.out.println("�ļ����ǿյ�!");
                // } else {
                    // for(File file2: files) {
                        // if(file2.getName().contains("����")) {
                            // continue;
                        // } else if(file2.isDirectory() && !file2.getName().matches("\\d+")) {
                            // getUserDirectory(file2.getAbsolutePath(), list);
                        // } else if(file2.isDirectory() && file2.getName().matches("\\d+")) {
                            // list.add(file2);
                        // }
                    // }
                // }
            // }
        // } else {
            // System.out.println("�ļ�������!");
        // }
        // return list;

		File file = new File(path);
		if(!file.exists()) {
            System.out.println("�ļ�������!");
            return list;
		}
		if(file.getName().contains("����")) {
            return list;
		}
		if(file.isDirectory() && file.getName().matches("\\d+")){
			list.add(file);
			return list;
		}
		File[] files = directory.listFiles();
		if(files.length == 0) {
            System.out.println("�ļ����ǿյ�!");
			return list;
		}
		for (File file2 : files) {
            if (file2.isDirectory() && !file.getName().contains("����")) {
                getUserDirectory(file2.getAbsolutePath(), list);
            }
        }
    }
}