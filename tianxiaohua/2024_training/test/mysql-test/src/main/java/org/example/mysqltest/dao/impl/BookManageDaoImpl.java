package org.example.mysqltest.dao.impl;

import org.example.mysqltest.bean.Book;
import org.example.mysqltest.dao.BookManageDao;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

public class BookManageDaoImpl implements BookManageDao {
    @Override
    public void createTable(String tableName) {

    }

    @Override
    public List<Book> queryAllBook() {
        return Collections.emptyList();
    }

    @Override
    public void addBook(Book book) {

    }

    @Override
    public void updateBook(Book book) {

    }

    @Override
    public void deleteBook(int id) {

    }

    @Override
    public Book queryBookById(int id) {
        return null;
    }

    @Override
    public Book queryBookByName(String name) {
        return null;
    }
}
