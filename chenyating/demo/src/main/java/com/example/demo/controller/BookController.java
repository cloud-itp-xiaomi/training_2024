package com.example.demo.controller;


import com.example.demo.bean.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    static List<Book> list=new ArrayList<>();
    static{
        Book book1=new Book(1,"文化苦旅","余秋雨",21,true,1);
        book1.setPath("http://localhost:8080/images/1.png");
        Book book2=new Book(2,"边城","沈从文",19,true,1);
        book2.setPath("http://localhost:8080/images/2.png");
        Book book3=new Book(3,"人生最美是清欢","林清玄",22,true,1);
        book3.setPath("http://localhost:8080/images/3.png");
        Book book4=new Book(4,"慢煮生活","汪曾祺",18,true,1);
        book4.setPath("http://localhost:8080/images/4.png");
        list.add(book1);
        list.add(book2);
        list.add(book3);
        list.add(book4);
    }

    @GetMapping("/list")
    public List<Book> getCarList(){
        return list;
    }

    @PostMapping("/delete/{id}")
    public Boolean delete(@PathVariable int id){
        for(Book b:list){
            if(b.getId()==id){
                list.remove(b);
                return true;
            }
        }
        return false;
    }

}
