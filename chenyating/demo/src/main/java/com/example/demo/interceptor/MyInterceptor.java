package com.example.demo.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyInterceptor implements HandlerInterceptor {
    // 创建日志对象
    private final Logger log = LoggerFactory.getLogger( this.getClass() );
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr(); // 获得客户端ip地址
        String path = request.getRequestURL().toString(); // 获得客户端请求路径
        log.info("-------- LogInterception.preHandle --- "); // 日志输出
        long startTime = System.currentTimeMillis(); // 获得当前系统时间(毫秒数)
        request.setAttribute("startTime", startTime); // 数据存放在request对象中
        //转化为日期格式
        Date nowTime = new Date(startTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(nowTime);
// 日志输出
        log.info("时间:"+now);
        log.info(ip+"访问了:"+path); // 日志输出
        return true; // 放行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("-------- LogInterception.postHandle --- ");
        log.info("控制器业务处理完毕");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("-------- LogInterception.afterCompletion --- ");
        long startTime = (Long) request.getAttribute("startTime"); // 读取request中存放的数据
        long endTime = System.currentTimeMillis();
        log.info("请求耗时(毫秒): " + (endTime - startTime) );
    }
}
