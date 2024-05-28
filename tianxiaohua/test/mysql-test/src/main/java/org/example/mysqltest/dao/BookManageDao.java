package org.example.mysqltest.dao;

import org.apache.ibatis.annotations.*;
import org.example.mysqltest.bean.Book;

import java.util.*;

@Mapper
public interface BookManageDao {
    void createTable(String tableName);
    List<Book> queryAllBook();
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int id);
    Book queryBookById(int id);
    Book queryBookByName(String name);
}
