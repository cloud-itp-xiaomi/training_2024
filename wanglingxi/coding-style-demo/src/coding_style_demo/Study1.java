package coding_style_demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Study1 {
    public static List< File > findNumberedDirectories(String path, List < File > list) {
    	if (list == null) {  
            list = new ArrayList<>();  
        } 
        File file = new File(path);
        if(!file.exists()) {
        	System.out.println("文件不存在!");
        	return list;
        }
        if(file.getName().contains("不算")) {
        	return list;
        }
        if(file.isDirectory() && file.getName().matches("\\d+")) {
        	list.add(file);
        	return list;
        }
        File[] files = file.listFiles();
        if(files == null || files.length == 0) {
        	System.out.println("文件夹是空的!");
        	return list;
        }
        for(File file2: files) {
            if(file2.getName().contains("不算")) {
                continue;
            } 
            if(file2.isDirectory()){
            	if (file2.getName().matches("\\d+")) {  
                    list.add(file2);  
                } else {  
                	findNumberedDirectories(file2.getAbsolutePath(), list);  
                }  
            }
        }
        return list;
    }
}