package com.lx.collector.pojo;

import com.google.gson.Gson;
import com.lx.collector.utils.GetBeanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件监控监听器类，负责监听指定文件的内容变化
 */
@Data
@Component
@DependsOn(value = "getBeanUtil")
@NoArgsConstructor
public class LogFileListener extends FileAlterationListenerAdaptor {

    private File file;
    private List<String> newLogs;
    private long lastModified;
    private long lastPosition;
    private String hostName;
    private RocketMQTemplate rocketMQTemplate = GetBeanUtil.getBean(RocketMQTemplate.class);
    private static String TOPIC_LOG = "HOST_LOG_TOPIC";//日志文件消息主题

    public LogFileListener(File file){
        this.file = file;
        lastPosition = 0L;
        newLogs = new ArrayList<>();
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileChange(File file) {
        // 当文件内容发生变化时执行
            if (this.file.getAbsolutePath().equals(file.getAbsolutePath())) {
            try {
                if (file.lastModified() > lastModified) {
                    List<String> newLogs = readNewLogs(file, lastPosition);
                    if (newLogs.size() > 0) {
                        LogMessage logMessage = new LogMessage();
                        logMessage.setHostName(hostName);
                        logMessage.setFile(file.toString());
                        logMessage.setLogs(newLogs);
                        Gson gson = new Gson();
                        String logGson = gson.toJson(logMessage);
                        rocketMQTemplate.convertAndSend(LogFileListener.TOPIC_LOG, logGson);//发送消息至消息队列"HOST_LOG_TOPIC"主题
                        System.out.println("send a message to " + LogFileListener.TOPIC_LOG);
                        System.out.println(logMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            lastModified = file.lastModified();
            lastPosition = file.length();
        }
    }

    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStart(observer);
    }

    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStop(observer);
    }

    /**
     * 读取文件增日志
     * @param file 监控日志文件
     * @param lastPosition 上次读取的文职
     * @return 新增日志
     * @throws IOException I/O异常
     */
    public static List<String> readNewLogs(File file, long lastPosition) throws IOException {
        List<String> newLogs = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(lastPosition);
            String line;
            while ((line = raf.readLine()) != null) {
                String newLog = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();
                //不添加空行
                if (!newLog.isEmpty()) {
                    newLogs.add(newLog);
                }
            }
        }
        return newLogs;
    }
}
