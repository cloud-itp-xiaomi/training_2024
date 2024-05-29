package org.xiaom.yhl.collector.service.impl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.xiaom.yhl.collector.service.CpuUsageService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * ClassName: CpuUsageServiceImpl
 * Package: org.xiaom.yhl.collector.service.impl
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/29 21:01
 * @Version 1.0
 */
@Service
public class CpuUsageServiceImpl implements CpuUsageService {
    @Override
    public double getCpuUsage() throws Exception {
        String host = "192.168.209.131";
        String user = "root";
        String password = "123456";
        int port = 22;

        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand("grep 'cpu ' /proc/stat");
        channel.setErrStream(System.err);
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));

        channel.connect();

        String line = reader.readLine();
        System.out.println("Command output: " + line);

        channel.disconnect();
        session.disconnect();

        if (line != null) {
            String[] values = line.trim().split("\\s+");
            // values[0] will be "cpu", skip it and parse the numbers
            long idleTime = Long.parseLong(values[4]);
            long totalTime = 0;
            for (int i = 1; i < values.length; i++) {
                totalTime += Long.parseLong(values[i]);
            }
            long previousIdleTime = 0; // 读取上一次的 idleTime
            long previousTotalTime = 0; // 读取上一次的 totalTime
            long idleTimeDelta = idleTime - previousIdleTime;
            long totalTimeDelta = totalTime - previousTotalTime;
            return 1 - ((double) idleTimeDelta / totalTimeDelta);
        }
        return 0.0;
    }
}
