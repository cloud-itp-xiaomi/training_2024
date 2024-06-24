package com.lx.server.store.impl;

import com.lx.server.common.StatusCode;
import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogResult;
import com.lx.server.pojo.ResLog;
import com.lx.server.store.LogStorage;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class LocalLogStorage implements LogStorage {

    private final String parent = "E:\\java_study\\javaProjects\\LogCollect\\server\\src\\main\\java\\local-logs";//存储日志文件的目录
    private final static LocalLogStorage localLogStorage = new LocalLogStorage();

    private LocalLogStorage() {

    }

    //单例模式
    public static LocalLogStorage getLocalStorage() {
        return localLogStorage;
    }

    @Override
    public boolean storeLog(LogMessage logMessage) {
        String hostName = logMessage.getHostName();
        String filePath = logMessage.getFile();
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
        String curTime = formatter.format(new Date(currentTimeMillis));
        //去除目录保留文件名
        File file = new File(filePath);
        String fileName = file.getName();
        File file1 = createFile(hostName, fileName, curTime);
        for (String log : logMessage.getLogs()) {
            try (FileWriter fw = new FileWriter(file1)) {
                fw.write(log);
                fw.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("store the logs failed!!");
                return false;
            }
        }
        return true;
    }

    @Override
    public LogResult queryLog(String hostName, String filePath) {
        ResLog resLog = new ResLog();
        resLog.setHostName(hostName);
        resLog.setFile(filePath);
        List<String> logs = new ArrayList<>();
        //去除目录保留文件名
        File file = new File(filePath);
        String fileName = file.getName();
        List<File> files = listFiles(hostName, fileName);
        for (File file1 : files) {
            //读取文件
            try (Scanner sc = new Scanner(new FileReader(file1))) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!line.trim().isEmpty()) {
                        logs.add(line);
                    }
                }
            } catch (FileNotFoundException e) {
                return new LogResult(StatusCode.FILE_NOT_EXIST.getCode(), StatusCode.FILE_NOT_EXIST.getMsg(), null);
            }
        }
        resLog.setLogs(logs);
        return new LogResult(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), resLog);
    }

    public File createFile(String hostName, String fileName, String curTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(hostName).append("-");
        sb.append(fileName).append("-");
        sb.append(curTime).append(".log");
        String child = sb.toString();
        File file1 = new File(parent, child);
        if (!file1.exists()) {
            try {
                boolean res = file1.createNewFile();
                if (res) {
                    System.out.println(file1.getName() + " create successfully");
                } else {
                    System.out.println(file1.getName() + " create failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file1;
    }

    //获取指定目录下满足要求的文件
    public List<File> listFiles(String hostName, String fileName) {
        List<File> res = new ArrayList<>();
        File dir = new File(parent);
        if (!dir.exists()) {
            System.out.println("指定目录不存在");
            return null;
        }
        String regex = "^" + hostName + "-" + fileName + "-" + "\\d+" + ".log" + "$";
        Pattern pattern = Pattern.compile(regex);
        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName1 = file.getName();//获取文件的绝对路径
            Matcher matcher = pattern.matcher(fileName1);
            if (matcher.matches()) {
                res.add(file);
            }
        }
        return res;
    }
}
