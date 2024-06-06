package com.example.demo.controller;

import com.example.demo.bean.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    static List<Book> list=new ArrayList<>();
    static{
//        list.add(new Book(1,"JavaEE开发实战",60,true,2));
//        list.add(new Book(2,"Vue开发实战",50,true,1));
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