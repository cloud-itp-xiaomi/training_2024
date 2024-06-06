package com.example.demo.controller;

import com.example.demo.bean.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/th")
public class ThController {
//    @RequestMapping("/test1")
//    public String test1( Model model ){
//        Student student = new Student(2021001,"小明");
//        model.addAttribute("stu", student);
//        model.addAttribute("count",666);
//        return "th/test1";
//    }

//    @RequestMapping("/test1")
//    public String test1( Model model ) {
//        Student student = new Student(2021001, "小明");
//        model.addAttribute("stu", student);
//        model.addAttribute("site", "武汉科技大学");
//        model.addAttribute("url", "https://www.wust.edu.cn");
//        model.addAttribute("imgURL", "/images/苏轼.jpg");
//        return "th/test1";
//    }

//    @RequestMapping("/test1")
//    public String test1( Model model ){
//        List<Student> list = new ArrayList<Student>();
//        list.add(new Student(2021001,"小明"));
//        list.add(new Student(2021002,"小丽"));
//        list.add(new Student(2021003,"小王"));
//        model.addAttribute("list",list);
//        return "th/test1";
//    }

//    @RequestMapping("/test1")
//    public String test1( Model model ){
//        Student student = new Student(2020001,"小明");
//        model.addAttribute("stu", student);
//        return "th/test1";
//    }
//    @PostMapping("/test2")
//    @ResponseBody
//    public Student test2(Integer id,String name){
//        Student student = new Student(id, name);
//        return student;
//    }

    static List<Student> stuList = new ArrayList<Student>();
    static {
        stuList.add(new Student(2021001, "小明"));
        stuList.add(new Student(2021002, "小丽"));
        stuList.add(new Student(2021003, "小王"));
    }
    // url: localhost:8080/th/
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("list", stuList);
        return "th/index";
    }

    @RequestMapping("/create/{id}/{name}")
    public String create(Model model,@PathVariable("id")Integer id,@PathVariable("name")String name){
        Student student=new Student(id,name);
        stuList.add(student);
        model.addAttribute("stu", student);
        return "th/create";
    }

    // url: localhost:8080/th/edit/2021001
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Student student = findById(id);
        model.addAttribute("stu", student);
        return "th/edit";
    }
    // url: localhost:8080/th/save 提交值为id和name post方式
    @PostMapping("/save")
    public String save(Integer id, String name) {
        Student student = findById(id);
        if (student != null) student.setName(name);
        return "redirect:/th/";
    }
    // url: localhost:8080/th/delete/2020001
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Student student = findById(id);
        stuList.remove(student);
        return "redirect:/th/"; // redirect:表示重定向
    }
    // 内部私有方法，外部不能访问
    private Student findById(Integer id) {
        Student student = null;
        for (Student s : stuList) {
            if (s.getId().intValue() == id.intValue()) {
                student = s;
            }
        }
        return student;
    }

}