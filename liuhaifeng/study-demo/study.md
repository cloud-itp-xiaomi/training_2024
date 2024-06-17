# 优化代码

```java
public class Study {
    public static List<File> getUserDirectory(String path, List<File> list) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件不存在!");
            return list;
        }
        if (isCount(file)) {
            return list;
        }
        if (file.isDirectory() && file.getName().matches("\\d+")) {
            list.add(file);
            return list;
        }
        File[] files = file.listFiles();
        if (files.length == 0) {
            System.out.println("文件夹是空的!");
            return list;
        }
        for (File file2 : files) {
            getUserDirectory(file2.getAbsolutePath(), list);
        }
        return list;
    }

    private static boolean isCount(File file) {
        return file.getName().contains("不算");
    }
}
```

