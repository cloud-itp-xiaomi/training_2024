package com.example.mi1.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author uchin/李玉勤
 * @date 2023/6/17 11:10
 * @description 响应工具类
 */
public class ResponseUtils {
    /**
     * 向前端输出JSON字符串，设置字符集是 UTF-8
     *
     * @param response response
     * @param json     json 串
     * @throws IOException e
     */
    public static void sendJSON(HttpServletResponse response, String json) throws IOException {
        // 一定要在获取流之前调用，否则无效
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8"); // 有时不写也可以
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();// 在springboot、springmvc中，必须加此行代码，否则可能报错
        out.close();
    }

    /**
     * * 向前端输出JSON字符串，设置字符集是 UTF-8
     *
     * @param response response
     * @param obj      对象
     * @throws IOException e
     */
    public static void sendJSON(HttpServletResponse response, Object obj) throws IOException {
        // 调用 jackson，将javabean转换成json字符串
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(obj);
        sendJSON(response, json);
    }

}
