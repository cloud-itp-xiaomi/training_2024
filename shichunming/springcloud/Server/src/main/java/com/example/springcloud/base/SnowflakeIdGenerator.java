package com.example.springcloud.base;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @ClassName SnowflakeIdWorker
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-05 11:00
 **/
@Component
public class SnowflakeIdGenerator {
    // 起始的时间戳，可以设置为服务第一次上线的时间，这里假设为2024-01-01 00:00:00对应的毫秒数
    private static final long START_STAMP = 1672531200000L;
    // 数据中心ID位数，最大值为2^10=1024
    private static final long DATA_CENTER_ID_BITS = 10L;
    // 机器标识位数，最大值为2^10=1024
    private static final long WORKER_ID_BITS = 10L;
    // 序列号位数，最大值为2^12=4096
    private static final long SEQUENCE_BITS = 12L;
    // 数据中心ID左移位数
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS;
    // 机器ID左移位数
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS + DATA_CENTER_ID_BITS;
    // 时间戳左移位数
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + DATA_CENTER_ID_BITS + WORKER_ID_BITS;
    // 序列掩码，确保序列号始终在12位内
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    @Value("${snowflake.dataCenterId}")
    private long dataCenterId;  // 数据中心ID
    @Value("${snowflake.workerId}")
    private long workerId;      // 机器标识
    private long sequence = 0L;  // 序列号
    private long lastTimestamp = -1L; // 上次生成ID的时间戳


    public Long getDataCenterId() {
        return dataCenterId;
    }
    public Long getWorkerId() {
        return workerId;
    }
    public SnowflakeIdGenerator() {
    }

    public SnowflakeIdGenerator(Long dataCenterId, Long workerId) {
        if (dataCenterId > maxDataCenterId() || dataCenterId < 0) {
            throw new IllegalArgumentException("Data center ID can't be greater than " + maxDataCenterId() + " or less than 0");
        }
        if (workerId > maxWorkerId(dataCenterId) || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + maxWorkerId(dataCenterId) + " or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明发生时钟回拨，需要做相应处理
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
        }

        // 如果是同一时间生成的，则进行sequence加1操作
        if (lastTimestamp == timestamp) {
            sequence = (sequence+1) & SEQUENCE_MASK;
            // 如果sequence溢出，需要等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        // 更新lastTimestamp
        lastTimestamp = timestamp;

        // 生成ID
        return ((timestamp - START_STAMP) << TIMESTAMP_LEFT_SHIFT) | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    /**
     * @Description: 阻塞到下一个毫秒，直到获得新的时间戳
     * @Param: lastTimestamp 上次生成ID的时间截
     * @return: long当前时间戳
     * @Author: 石春明/心木
     * @Date: 11:07 2024/6/5
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * @Description: 返回以毫秒为单位的当前时间
     * @Param:
     * @return: long当前时间(毫秒)
     * @Author: 石春明/心木
     * @Date: 11:07 2024/6/5
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    private long maxWorkerId(long dataCenterId) {
        return ~(-1L << WORKER_ID_BITS);
    }

    private long maxDataCenterId() {
        return ~(-1L << DATA_CENTER_ID_BITS);
    }
}
