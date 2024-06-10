package com.example.bookdemo.service;

import com.example.bookdemo.dao.Book;
import com.example.bookdemo.dto.BookDto;

public interface BookService {
    BookDto getBookById(long id);

    Long addNewBook(BookDto bookDto);

    void deleteBookById(long id);

    BookDto updateBookById(long id, String name, double price);
}
