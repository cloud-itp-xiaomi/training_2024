package com.example.mi1.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * @author uchin/李玉勤
 * @date 2023/4/23 16:05
 * @description
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 单文件上传
     */
    public static String fileUpload(MultipartFile file, String parentDir) {
        if (file.isEmpty()) {
            return "No file is selected.";
        }
        File temp = new File(parentDir);
        if (!temp.exists()) {
            boolean mkdirs = temp.mkdirs();
        }
        String fileName = file.getOriginalFilename(); //获取上传文件原来的名称
        File localFile = new File(PathUtils.concat(parentDir, fileName));
        try {
            file.transferTo(localFile); //把上传的文件保存至本地
            log.info(file.getOriginalFilename() + " was uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return "File uploaded successfully.";
    }

    /**
     * 多文件上传
     */
    public static String multiFileUpload(List<MultipartFile> files, String parentDir) {
        if (files.isEmpty()) {
            return "No file is selected.";
        }
        File temp = new File(parentDir);
        if (!temp.exists()) {
            boolean mkdirs = temp.mkdirs();
        }
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename(); //获取上传文件原来的名称
            int size = (int) file.getSize();
            log.info(fileName + "-->" + size);
            if (file.isEmpty()) {
                return "The file is empty.";
            } else {
                File localFile = new File(PathUtils.concat(parentDir, fileName));
                try {
                    file.transferTo(localFile); //把上传的文件保存至本地
                    log.info(fileName + " was uploaded successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return "Files uploaded successfully.";
    }

    /**
     * 读取文本文件内容
     */
    public static String readTextFile(String filePath) throws Exception {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(line);
            }
        }
        return result.toString().length() >= System.lineSeparator().length()
                ? result.substring(System.lineSeparator().length())
                : null;
    }

    /**
     * 输出文件夹下文本文件内容
     */
    @Deprecated
    public static void printTextFile(String dirPath) throws Exception {
        File srcFile = new File(dirPath);
        if (srcFile.isDirectory()) {
            File[] next = srcFile.listFiles();
            for (int i = 0; i < Objects.requireNonNull(next).length; i++) {
                if (!next[i].isDirectory()) {
                    BufferedReader br = new BufferedReader(new FileReader(next[i]));
                    List<String> arr1 = new ArrayList<>();
                    String contentLine;
                    while ((contentLine = br.readLine()) != null) {
                        // JSONObject js = JSONObject.parseObject(contentLine);
                        arr1.add(contentLine);
                    }
                    System.out.println(arr1);
                }
            }
        }
    }

    /**
     * 列举dirPath文件夹下所有的文件，递归查询所有文件
     */
    public static List<String> showFiles(String dirPath) {
        List<String> filesList = new ArrayList<>();
        Vector<String> ver = new Vector<String>();  //用做堆栈
        ver.add(dirPath);
        while (ver.size() > 0) {
            File[] files = new File(ver.get(0)).listFiles();  //获取该文件夹下所有的文件(夹)名
            ver.remove(0);
            assert files != null;
            for (File file : files) {
                String tmp = file.getAbsolutePath();
                if (file.isDirectory())  //如果是目录，则加入队列。以便进行后续处理
                    ver.add(tmp);
                else {
                    filesList.add(tmp); //如果是文件，则直接输出文件名
                }
            }
        }
        return filesList;
    }

    /**
     * 列举dirPath当前文件夹下所有的文件，不递归查询
     */
    public static List<String> showCurFiles(String dirPath) {
        List<String> filesList = new ArrayList<>();
        File file = new File(dirPath);
        if (!file.isDirectory())
            return null;
        File[] files = file.listFiles();
        assert files != null;
        for (File value : files) {
            filesList.add(value.toString());
        }
        return filesList;
    }

    /**
     * 文件拷贝
     */
    public static void copyFile(String srcFilePath, String destFilePath) throws IOException {
        // 获取要复制的文件
        File srcFile = new File(srcFilePath);
        // 文件输入流，用于读取要复制的文件
        FileInputStream fileInputStream = new FileInputStream(srcFile);
        // 要生成的新文件（指定路径如果没有则创建）
        File destFile = new File(destFilePath);
        // 获取父目录
        File fileParent = destFile.getParentFile();
        // 判断是否存在
        if (!fileParent.exists()) {
            // 创建父目录文件夹
            boolean mkdirs = fileParent.mkdirs();
        }
        // 判断文件是否存在
        if (!destFile.exists()) {
            // 创建文件
            boolean newFile = destFile.createNewFile();
        }
        // 新文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int len;
        // 将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
        while ((len = fileInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
            fileOutputStream.flush();
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     * 清空文件夹
     */
    public static void clearFolder(File folderPath) {
        if (folderPath.exists()) {
            File[] files = folderPath.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子文件夹中的文件和文件夹
                        clearFolder(file);
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }
        }
    }
}
