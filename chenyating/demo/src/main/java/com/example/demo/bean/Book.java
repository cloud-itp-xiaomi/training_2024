package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private double price;
    private boolean active;
    private int count;

    private String author;
    private String path;

    public Book (int id,String title,String author,double price,boolean active,int count){
        this.id=id;
        this.title=title;
        this.author=author;
        this.price=price;
        this.active=active;
        this.count=count;
    }
}

