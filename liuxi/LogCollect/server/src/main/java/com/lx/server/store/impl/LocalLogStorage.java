package com.lx.server.store.impl;

import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogResult;
import com.lx.server.pojo.ResLog;
import com.lx.server.store.LogStorage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class LocalLogStorage implements LogStorage {

    private final String parent = "E:\\java_study\\javaProjects\\LogCollect\\server\\src\\main\\java\\local-logs";//存储日志文件的目录

    @Override
    public boolean storeLog(LogMessage logMessage) {
        String hostName = logMessage.getHostName();
        String file = logMessage.getFile();
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
        String curTime = formatter.format(new Date(currentTimeMillis));
        File file1 = createFile(hostName, file, curTime);
        for(String log : logMessage.getLogs()) {
            try (FileWriter fw =new FileWriter(file1)) {
                fw.write(log);
                fw.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public LogResult queryLog(String hostName, String file) {
        ResLog resLog = new ResLog();
        resLog.setHostName(hostName);
        resLog.setFile(file);
        return null;


    }

    public File createFile(String hostName , String file , String curTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(hostName).append("-");
        sb.append(file).append("-");
        sb.append(curTime).append(".log");
        String child = sb.toString();
        File file1 = new File(parent, child);
        if(!file1.exists()) {
            try {
                boolean res = file1.createNewFile();
                if(res) {
                    System.out.println(file1.getName() + "create successfully");
                }else {
                    System.out.println(file1.getName() + "create failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file1;
    }

    //获取指定目录下满足要求的文件
    public List<File> listFiles(String hostName, String file){
        List<File> res = new ArrayList<>();
        File dir = new File(parent);
        if (!dir.exists()) {
            System.out.println("指定目录不存在。");
            return null;
        }
        String regex = "^" + hostName + "-" + file + "-" + "\\d+" + ".log" + "$";
        Pattern pattern = Pattern.compile(regex);
        File[] files = dir.listFiles();
        for(File file1 : files) {
            String filename = file1.getName();
            Matcher matcher = pattern.matcher(filename);
            if(matcher.matches()) {
                res.add(file1);
            }
        }
        return res;
    }

}
