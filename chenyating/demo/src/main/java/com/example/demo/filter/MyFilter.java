package com.example.demo.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(urlPatterns = {"/*"} ) //urlPatterns指定的请求将被拦截，filterName值可以省略
public class MyFilter implements Filter {
    private List<String> IPList=new ArrayList<>(); //黑名单
    private List<String> Words=new ArrayList<>(); //敏感词汇

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Words.add("TMD"); //敏感词汇
        Words.add("NMD");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {

//        servletResponse.setContentType("text/html;charset=utf-8"); //设置编码格式，防止输出乱码
//        servletResponse.getWriter().print("被拦截了,3s之后跳转/login");
//        ((HttpServletResponse)servletResponse).setHeader("refresh", "3;url=http://localhost:8080/login");

        String ip = servletRequest.getRemoteAddr(); //获得客户端 ip 地址
        String word = servletRequest.getParameter("word"); //获得url请求中的word参数
        servletResponse.setContentType("text/html;charset=utf-8");
        if( IPList.contains(ip) ) {
            servletResponse.getWriter().println("你已被列入黑名单!");
        }
        else if( word != null && Words.contains( word.toUpperCase() ) ) { //敏感词汇判断
            servletResponse.getWriter().println("提交数据有敏感词汇! 你已被列入黑名单!");
            IPList.add(ip);
        }
        else{
            filterChain.doFilter(servletRequest,servletResponse); //其他情况直接放行
        }
    }

    @Override
    public void destroy() {

    }

}