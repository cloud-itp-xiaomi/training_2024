package com.example.mi1.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * @author uchin/李玉勤
 * @date 2023/3/29 22:07
 * @description 日期工具类
 */
public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String STANDARD_TIME_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String PATH_TIME_FORMAT_LONG = "yyyy/MM/dd HH:mm:ss.SSS";
    public static String PATH_DAY_FORMAT = "yyyy/MM/dd";
    public static String PATH_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String YYYYMMDDHHMM_SPLASH = "yyyy/MM/dd HH:mm";

    public static String YYYYMMDD = "yyyyMMdd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYYMMDDHHMMSSSS = "yyyyMMddHHmmssSS";

    public static String YYYYMMDD_CN = "yyyy年MM月dd日";
    public static String DATE_BEGIN = " 00:00:00";
    public static String DATE_END = " 23:59:59";
    public static String EEE_MMM_DD_HH_MM_SS_ZZZ_YYY = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static String[] formats = new String[]{
            YYYY_MM_DD,
            YYYY_MM_DD_HH_MM_SS,
            YYYYMMDDHHMM_SPLASH,
            YYYY_MM_DD_HH_MM,
            STANDARD_TIME_FORMAT_LONG,
            "yyyy-MM-dd HH:mm:ss:SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSS X",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ss",
            PATH_DAY_FORMAT,
            PATH_TIME_FORMAT,
            PATH_TIME_FORMAT_LONG,
            YYYY_MM_DD,
            YYYYMMDDHHMMSS,
            "yyyyMMdd HH:mm:ss.SSS",
            "yyyyMMdd HH:mm:ss",
            YYYYMMDD,
            "dd/MM/YYYY HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS+08:00"
    };

    private DateUtils() {
    }

    /**
     * Date 对象按指定格式转成 String
     *
     * @param date   Date对象
     * @param format 格式
     * @return String
     */
    public static String parse(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 传入时间戳，返回指定格式字符串
     *
     * @param datetime 时间戳
     * @param format   格式
     * @return 字符串
     */
    public static String parse(long datetime, String format) {
        Date date = new Date(datetime);
        return parse(date, format);
    }

    /**
     * 字符串尝试按指定格式转 Date 对象
     *
     * @param datetime 描述时间的字符串
     * @return 成功返回 Date 对象；失败返回 null
     */
    public static Date parse(String datetime) {
        try {
            return parseDate(datetime, formats);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }

    public static String dateFormatTransfer(String srcFormat, String datetime, String dstFormat) {
        DateFormat srcDateFormat = new SimpleDateFormat(srcFormat);
        DateFormat dstDateFormat = new SimpleDateFormat(dstFormat);
        try {
            Date d = srcDateFormat.parse(datetime);
            if (d != null) {
                return dstDateFormat.format(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得日期年月日
     *
     * @param date
     * @return
     */
    public static int[] getDateYMD(Date... date) {
        Date tmpDate = new Date();
        if ((date != null) && (date.length > 0)) {
            tmpDate = date[0];
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(tmpDate);
        return new int[]{
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)};
    }

    public static int getOffDays(long l) {
        return (int) ((System.currentTimeMillis() - l) / 60000L) / 1440;
    }

    public static int getOffMinutes(long l) {
        return (int) ((System.currentTimeMillis() - l) / 60000L);
    }

    public static String getLastNQuarterBeginDate(int n) {
        return getLastNQuarterBeginDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    public static String getLastNQuarterBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n * 3);

        int q = getQuarter(c.get(Calendar.MONTH));

        c.set(Calendar.MONTH, 3 * (q - 1));
        c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
        return format.format(c.getTime());
    }

    public static String getLastNQuarterEndDate(int n) {
        return getLastNQuarterEndDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    private static int getQuarter(int month) {
        if (month <= 2 && month >= 0) {
            return 1;
        }
        if (month <= 5 && month >= 3) {
            return 2;
        }
        if (month <= 8 && month >= 6) {
            return 3;
        }
        if (month <= 11 && month >= 9) {
            return 4;
        }
        throw new IllegalStateException("月份错误");
    }

    public static String getLastNQuarterEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n * 3);

        int q = getQuarter(c.get(Calendar.MONTH));

        c.set(Calendar.MONTH, 2 + 3 * (q - 1));

        c.set(Calendar.DATE, c.getMaximum(Calendar.DAY_OF_MONTH));

        return format.format(c.getTime());
    }

    public static String getLastNMonthBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n);
        c.set(Calendar.DATE, c.getMinimum(Calendar.DAY_OF_MONTH));
        return format.format(c.getTime());
    }

    public static String getLastNMonthBeginDate(int n) {
        return getLastNMonthBeginDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    public static String getLastNMonthEndDate(int n) {
        return getLastNMonthEndDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    public static String getLastNMonthEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        return format.format(c.getTime());
    }

    public static Date getBeforeNDayDate(long n, Date... date) {
        Date tmpDate;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = new Date();
        }
        Long dateTime = tmpDate.getTime() - n * 24 * 60 * 60 * 1000L;
        return new Date(dateTime);
    }

    /**
     * 获取 n 秒前时间 ，如一天前：24*60*60
     *
     * @param n    单位为妙
     * @param date 默认为当前日期
     * @return
     */
    public static Date getNSeconBefore(long n, Date... date) {
        Date tmpDate;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = new Date();
        }
        Long dateTime = tmpDate.getTime() - n * 1000;
        return new Date(dateTime);
    }

    /**
     * 获取 n 天之后的日期
     */
    public static String getLastNDayDate(int n, Date... date) {
        Date tmpDate;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(tmpDate);
        c.add(Calendar.DAY_OF_YEAR, n);
        return parse(c.getTime(), YYYY_MM_DD_HH_MM_SS);
    }

    public static String getLastNDayDate(int n, String date) {
        return getLastNDayDate(n, parse(date));
    }

    /**
     * 获得N个月后日期
     *
     * @param date
     * @param n
     * @return
     */
    public static String getLastNMonthDate(int n, Date... date) {
        Date tmpDate;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(tmpDate);
        c.add(Calendar.MONTH, n);

        return parse(c.getTime(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获得N个月后日期
     *
     * @param n
     * @param date
     * @return
     */
    public static String getLastNMonthDate(int n, String date) {
        if (StringUtils.isNotBlank(date)) {
            return getLastNMonthDate(n, parse(date));
        } else {
            return getLastNMonthDate(n);
        }
    }

    public static String getLastNYearBeginDate(int n) {
        return getLastNYearBeginDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    public static String getLastNYearBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, n);
        c.set(c.get(Calendar.YEAR), 0, 1);
        return format.format(c.getTime());
    }

    public static String getLastNYearEndDate(int n) {
        return getLastNYearEndDate(n, new SimpleDateFormat(YYYY_MM_DD));
    }

    public static String getLastNYearEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, n);
        c.set(c.get(Calendar.YEAR), 11, 31);
        return format.format(c.getTime());
    }

    public static String getCurrentWeekDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        String weekDayStr = null;
        switch (weekDay) {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }
        return weekDayStr;
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static int getCurrentHourOfDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static String getAMPM() {
        int hour = getCurrentHourOfDay();
        String amPM = "";
        if (hour <= 12) {
            amPM = "上午";
        } else if (hour <= 18) {
            amPM = "下午";
        } else if (hour < 24) {
            amPM = "晚上";
        }
        return amPM;
    }

    /**
     * 两日前相差天数
     *
     * @param end
     * @param begin
     * @return
     */
    public static long getDifferDays(Date end, Date begin) {
        long quot;
        quot = end.getTime() - begin.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return quot;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param beginDate 较小的时间
     * @param endDate   较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static long getBetweenDay(Date beginDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        return (time2 - time1) / (1000 * 3600 * 24);
    }

    /**
     * 实现 日期 加减指定 天数
     *
     * @param srcDate
     * @param amount
     * @return
     */
    public static Date dateAdd(Date srcDate, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        c.add(Calendar.DAY_OF_YEAR, amount);
        return c.getTime();
    }

    /**
     * 实现日期加减指定小时
     *
     * @param srcDate
     * @param hour
     * @return
     */
    public static Date dateAddHour(Date srcDate, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 获取当天0点的时间
     *
     * @return
     */
    public static Date getTodayZeroTime() {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取 n 小时前，以及当前时间<br>
     * keys: beginDate,endDate
     *
     * @return
     */
    public static Map<String, Date> getNHourBeforeRangeMap(int hours) {
        if (hours < 0) {
            hours = 1;
        }
        Map<String, Date> dateMap = new HashMap<String, Date>();
        Date beginDate = DateUtils.getNSeconBefore(Long.valueOf(hours * 60 * 60)); // n 小时前
        dateMap.put("beginDate", beginDate);
        dateMap.put("endDate", new Date());
        return dateMap;
    }

    /**
     * 日期天数增加
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        if (null == date || "".equals(date)) {
            return null;
        }
        if (days == 0) {
            return date;
        }
        Locale locale = Locale.getDefault();
        Calendar calendar = new GregorianCalendar(locale);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Date getTimeFromCST(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(EEE_MMM_DD_HH_MM_SS_ZZZ_YYY, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
