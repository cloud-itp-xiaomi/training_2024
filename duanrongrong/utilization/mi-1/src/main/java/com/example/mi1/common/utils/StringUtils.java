package com.example.mi1.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author uchin/李玉勤
 * @date 2023/3/29 22:22
 * @description
 */
public class StringUtils {
    /**
     * 判断字符串是否全是中文
     */
    public static boolean isAllChinese(String str) {
        if (str == null)
            return false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
