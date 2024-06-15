package com.example.mi1.common.constant;

/**
 * @author uchin/李玉勤
 * @date 2023/5/22 11:47
 * @description
 */
public class CommonConst {
    public enum Time {
        // 业务中经常使用的时间的格式化
        GENERAL("yyyy-MM-dd HH:mm:ss"),
        // 精确到天的格式化
        SIMPLE("yyyy-MM-dd");

        private final String format;

        Time(String format) {
            this.format = format;
        }

        public String format() {
            return format;
        }
    }

}
