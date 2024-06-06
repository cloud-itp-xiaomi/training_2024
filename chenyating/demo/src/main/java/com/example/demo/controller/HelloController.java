package com.example.demo.controller;

import com.example.demo.bean.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String index( Model model ) {
        String  userName = "wustzz";
        model.addAttribute("name", userName);
        return "hello/index";
    }

    @RequestMapping("/test")
    public String test( Model model ) {
        Student s = new Student(2020001,"小明");
        model.addAttribute("stu", s );
        return "hello/test";
    }

    @RequestMapping("/students")
    @ResponseBody
    public List<Student> getAllStudents(){
        List<Student> list = new ArrayList<Student>();
        Student s1 = new Student(2020001,"小明");
        Student s2 = new Student(2020002,"小张");
        list.add(s1);
        list.add(s2);
        return list;
    }

    @PostMapping("/login")
    @ResponseBody
    public Boolean login(String username, String password) {
        if ("zz".equals(username) && "666".equals(password)) {
            return true;
        } else
            return false;
    }

}