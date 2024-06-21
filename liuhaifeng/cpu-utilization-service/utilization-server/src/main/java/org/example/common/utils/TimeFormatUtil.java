package org.example.common.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 时间格式转换工具类
 *
 * @author liuhaifeng
 * @date 2024/06/01/15:42
 */

public class TimeFormatUtil {

    /**
     * 秒级时间戳转LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime longToLocalDateTime(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转秒级时间戳
     *
     * @param localDateTime
     * @return
     */
    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime).getTime() / 1000;
    }
}
