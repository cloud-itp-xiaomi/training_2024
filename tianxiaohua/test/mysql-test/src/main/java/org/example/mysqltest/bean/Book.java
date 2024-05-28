package org.example.mysqltest.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author txh
 * @Date 2024/5/26 17:04
 * @Desc MySQLJavaBean
 */
@Data //包含了get，set和toString
@AllArgsConstructor //有参构造器 set
public class Book {
    private int id;
    private String name;
    private String author;
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
}
