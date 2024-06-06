package com.example.demo.controller;

import com.example.demo.bean.Student;
import com.example.demo.result.MyResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@ResponseBody
public class StudentController {
    static List<Student> list = new ArrayList<Student>();
    static{
        list.add(new Student(2020001,"小明"));
       list.add(new Student(2020002,"小张"));
    }

    @GetMapping("/")
    public MyResult<List<Student>> getAll(){
        MyResult<List<Student>> myResult =new MyResult<>();
        myResult.setResultCode(200);
        myResult.setMessage("成功查询");
        myResult.setData(list);

        return myResult;
    }

    @PostMapping("/")
     public MyResult<Boolean> addStudent(@RequestBody Student stu) {  //Integer id, String name,不能接收json数据
         //参数用自定义类型，加注解，可以把json对象封装成对象接收
         MyResult<Boolean> myResult =new MyResult<>();

         Boolean flag=true;
         if (findById(stu.getId())) {
             flag=false;
             myResult.setResultCode(400);
             myResult.setMessage("id重复，添加失败");
         }
         if(flag) {
             list.add(stu); //new Student(id,name)
             myResult.setResultCode(201);
             myResult.setMessage("添加成功");
         }

         myResult.setData(flag);
         return myResult;
     }
     private Boolean findById(Integer id) {
         for (Student s : list) {
             if (s.getId().intValue() == id.intValue())
                  return true;
         }
         return false;
     }


//    @GetMapping("/")
//    public List<Student> getAllStudents(){
//        return list;
//    }
//
//    @PostMapping("/")
//    public String addStudent(Integer id,String name) {
//          for(Student item : list){
//              if( item.getId().intValue() == id.intValue())
//                  return "该学生信息已存在，请勿重复添加";
//              else {
//                  Student s = new Student(id, name);
//                  list.add(s);
//                  return "添加成功";
//              }
//          }
//          return "操作结束";
//    }

}
