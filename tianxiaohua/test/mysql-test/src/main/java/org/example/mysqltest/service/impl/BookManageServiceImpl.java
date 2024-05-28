package org.example.mysqltest.service.impl;

import org.example.mysqltest.bean.Book;
import org.example.mysqltest.dao.BookManageDao;
import org.example.mysqltest.service.BookManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class BookManageServiceImpl implements BookManageService {
    private final BookManageDao bookManageDao;

    @Autowired
    public BookManageServiceImpl(BookManageDao bookManageDao) {
        this.bookManageDao = bookManageDao;
    }

    @Override
    public void createTable(String tableName) {
        bookManageDao.createTable(tableName);
    }

    @Override
    public List<Book> queryAllBook() {
        return bookManageDao.queryAllBook();
    }

    @Override
    public boolean addBook(Book book) {
        try{
            bookManageDao.addBook(book);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateBook(Book book) {
        try{
            bookManageDao.updateBook(book);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteBook(int id) {
        try{
            bookManageDao.deleteBook(id);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Book queryBookById(int id) {
        return bookManageDao.queryBookById(id);
    }

    @Override
    public Book queryBookByName(String bookName) {
        return bookManageDao.queryBookByName(bookName);
    }
}
