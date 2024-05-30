package com.xiaomi2024.txh.dubbo.spring.provider.dao;

import org.apache.ibatis.annotations.Mapper;
import com.xiaomi2024.txh.dubbo.spring.provider.bean.Book;

import java.util.List;

@Mapper
public interface BookManageDao {
    void createTable(String tableName);
    List<Book> queryAllBook();
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(String name);
    Book queryBookById(int id);
    Book queryBookByName(String name);
}
