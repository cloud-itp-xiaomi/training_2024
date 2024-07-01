package com.winter.factory;

import com.winter.domain.LogData;
import com.winter.req.LogQueryReq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用本地文件存储
 * */
public class LocalFileStore implements LogStore{

    private static final String root = "/usr/logs/";  //所有存储文件的根目录

    /**
     * 使用文件的方式存储日志，追加写入日志数据
     * */
    @Override
    public void storeData(LogData logData) {
        //组装log文件所在的目录
        String hostname = logData.getHostname();
        String file = logData.getFile();
        String logFile = root + hostname + file;
        Path path = Paths.get(logFile);

        //如果不存在path这个路径，则创建
        if (Files.notExists(path)){
            try {
                //创建父目录
                if (Files.notExists(path.getParent())){
                    Files.createDirectories(path.getParent());
                }
                //创建文件
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //写日志到对应的文件
        try {
            String logs = logData.getLogs();  //注意这里得到是一个字符串
            //将字符串转化为集合 [aaa, bbbb]
            List<String> logs_list = new ArrayList<>();
            String[] logs_split = logs.split(",");
            int first = 0;
            int last = logs_split.length-1;
            logs_split[first] = logs_split[first].substring(1, logs_split[first].length()-1);
            logs_split[last] = logs_split[last].substring(0, logs_split[last].length()-1);

            Files.write(
                    path,
                    Arrays.asList(logs_split), // 将字符串数组转换为列表
                    StandardOpenOption.APPEND, // 追加模式
                    StandardOpenOption.CREATE // 如果文件不存在则创建文件
            );
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    /***
     * 查询采用文件方式存储的日志数据
     * */
    @Override
    public List<LogData> queryData(LogQueryReq logQueryReq) {
        //组装查询路径
        String hostname = logQueryReq.getHostname();
        String file = logQueryReq.getFile();
        String logFile = root + hostname + file;
        Path path = Paths.get(logFile);

        //如果不存在这个文件，则直接返回null
        if (Files.notExists(path)){
            return null;
        }

        //如果存在，则返回数据
        //对于文件方式的存储比较特殊，这里将所有的信息都封装到一个类中
        List<LogData> res = new ArrayList<>();
        LogData logData = new LogData();
        logData.setId("0");  //这里只包含一个元素，硬编码，后期优化
        logData.setHostname(hostname);
        logData.setFile(file);

        //按行读取文件的内容
        try {
            List<String> lines = Files.readAllLines(path);
            logData.setLogs(lines.toString());
            res.add(logData);  //添加进集合
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
