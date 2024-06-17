package com.example.bookdemo.service;

import com.example.bookdemo.converter.BookConverter;
import com.example.bookdemo.dao.Book;
import com.example.bookdemo.dao.BookRepository;
import com.example.bookdemo.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BookServicesImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Override
    public BookDto getBookById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(RuntimeException::new);
        return BookConverter.BookToDto(book);

    }

    @Override
    public Long addNewBook(BookDto bookDto) {
        List<Book> bookList = bookRepository.findByName(bookDto.getName());
        if (!CollectionUtils.isEmpty(bookList)){
            throw new IllegalStateException("name:" + bookDto.getName() + "has been existed!" );
        }
//        可以优化查询其中id，按照递增方式增加ID
        Book book = bookRepository.save(BookConverter.BookDtoToBook(bookDto));
        return book.getId();
    }

    @Override
    public void deleteBookById(long id) {
        bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id:" + id + "isn't exist!"));
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateBookById(long id, String name, double price) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id:" + id + "isn't exist!"));
        if (StringUtils.hasLength(name) && !book.getName().equals(name)){
            book.setName(name);
        }
        if ( price >=0 && book.getPrice() != price){
            book.setPrice(price);
        }
        Book updatebook = bookRepository.save(book);
        return BookConverter.BookToDto(updatebook);
    }
}
