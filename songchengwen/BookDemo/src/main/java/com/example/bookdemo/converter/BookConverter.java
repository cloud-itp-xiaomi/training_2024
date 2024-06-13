package com.example.bookdemo.converter;

import com.example.bookdemo.dao.Book;
import com.example.bookdemo.dto.BookDto;

public class BookConverter {
    public static BookDto BookToDto(Book book){
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setPrice(book.getPrice());
        bookDto.setName(book.getName());
        return bookDto;
    }
    public static Book BookDtoToBook(BookDto bookDto){
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setName(bookDto.getName());
        book.setPrice(bookDto.getPrice());
        return book;
    }
}
