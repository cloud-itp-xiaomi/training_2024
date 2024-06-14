package com.example.mi1.service.impl;

import com.example.mi1.service.DockerTask;
import com.example.mi1.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MachineServiceImpl implements MachineService {

    private static final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

    private ThreadPoolExecutor threadPoolExecutor;
    private DockerTask task1;
    private DockerTask task2;
    private DockerTask task3;

    @Override
    public void start() {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            logger.warn("线程池已经在运行中。");
            return;
        }

        threadPoolExecutor = new ThreadPoolExecutor(
                10,
                20,
                1L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        logger.info("开始启动 Docker 任务...");
        task1 = new DockerTask("container1");
        task2 = new DockerTask("container2");
        task3 = new DockerTask("container3");

        threadPoolExecutor.execute(task1);
        threadPoolExecutor.execute(task2);
        threadPoolExecutor.execute(task3);
        logger.info("Docker 任务已启动。");
    }

    @Override
    public void stop() {
        stopTask1();
        stopTask2();
        stopTask3();
        stopThreadPool();
    }

    public void stopTask1() {
        if (task1 != null) {
            logger.info("正在停止 Docker 任务 container_1...");
            task1.stop();
        } else {
            logger.warn("Docker 任务 container_1 未初始化或已停止。");
        }
    }

    public void stopTask2() {
        if (task2 != null) {
            logger.info("正在停止 Docker 任务 container_2...");
            task2.stop();
        } else {
            logger.warn("Docker 任务 container_2 未初始化或已停止。");
        }
    }

    public void stopTask3() {
        if (task3 != null) {
            logger.info("正在停止 Docker 任务 container_3...");
            task3.stop();
        } else {
            logger.warn("Docker 任务 container_3 未初始化或已停止。");
        }
    }

    private void stopThreadPool() {
        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
            logger.warn("线程池已停止或未初始化。");
            return;
        }

        logger.info("正在停止线程池...");
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.warn("线程池未能在指定时间内终止。");
                threadPoolExecutor.shutdownNow();
                if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("线程池未能终止。");
                }
            }
        } catch (InterruptedException e) {
            logger.error("线程池关闭过程中断。", e);
            threadPoolExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("线程池已停止。");
    }
}
