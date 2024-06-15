package com.txh.xiaomi2024.work.service.util;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class IdGeneratorUtil {

    private static final SnowFlake standAloneSnowFlake = new SnowFlake(0L, 0L);
    private static final SnowFlake distributeSnowFlake = new SnowFlake(getWorkId(), getDataCenterId());

    public static long standAloneSnowFlake() {
        return standAloneSnowFlake.nextId();
    }

    public static long distributeSnowFlake() {
        return distributeSnowFlake.nextId();
    }

    /**
     * workId使用IP生成
     *
     * @return workId
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }

    /**
     * dataCenterId使用hostName生成
     *
     * @return dataCenterId
     */
    private static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i : ints) {
                sums = sums + i;
            }
            return (long) (sums % 32);
        } catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }

}

