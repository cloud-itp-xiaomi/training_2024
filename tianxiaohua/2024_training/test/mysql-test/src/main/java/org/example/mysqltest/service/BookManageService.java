package org.example.mysqltest.service;

import org.example.mysqltest.bean.Book;

import java.util.List;

public interface BookManageService {
    void createTable(String tableName);
    List<Book> queryAllBook();
    boolean addBook(Book book);
    boolean updateBook(Book book);
    boolean deleteBook(int id);
    Book queryBookById(int id);
    Book queryBookByName(String bookName);
}
