package com.example.demo.bean;

public class Student {
    private Integer id;  //学号
    private String name;  //姓名
    public Student() {
    }
    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
