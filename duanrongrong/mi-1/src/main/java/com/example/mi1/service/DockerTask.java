package com.example.mi1.service;

public class DockerTask implements Runnable {
    private final String containerName;
    private volatile boolean running = true;
    private Process process;

    public DockerTask(String containerName) {
        this.containerName = containerName;
    }

    public void stop() {
        running = false;
        try {
            // 构建 docker stop 命令
            String command = "docker stop " + containerName;
            ProcessBuilder builder = new ProcessBuilder(command.split("\\s+"));
            // 启动进程执行命令
            Process stopProcess = builder.start();
            // 等待命令执行完毕
            stopProcess.waitFor();
            System.out.println("Container " + containerName + " stopped successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            String command = "docker run --name " + containerName + " collector"; // Docker 启动命令
            process = Runtime.getRuntime().exec(command); // 启动进程执行命令

            // 等待进程结束
            while (running && process.isAlive()) {
                Thread.sleep(500); // 这里的 sleep 可以控制检查进程状态的频率
            }

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
