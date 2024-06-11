package com.example.mi1.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DockerTask implements Runnable {
    private final String containerName;

    public DockerTask(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public void run() {
        try {
            String command = "docker run --name " + containerName + " collector"; // Docker 启动命令
            Process process = Runtime.getRuntime().exec(command); // 启动进程执行命令
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // 读取命令执行的输出
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
            int exitCode = process.waitFor(); // 等待命令执行完毕
            if (exitCode == 0) {
                System.out.println("Container " + containerName + " started successfully.");
            } else {
                System.out.println("Failed to start container " + containerName + ". Exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
