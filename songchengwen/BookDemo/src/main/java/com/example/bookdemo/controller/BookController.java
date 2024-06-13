package com.example.bookdemo.controller;

import com.example.bookdemo.Response;
import com.example.bookdemo.dao.Book;
import com.example.bookdemo.dto.BookDto;
import com.example.bookdemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    @Autowired
    private BookService bookservice;
    @GetMapping("/Book/{id}")
    public Response<BookDto> getBookById(@PathVariable int id){
        return Response.newSuccess(bookservice.getBookById(id));
    }
    @PostMapping("/Book")
    public Response<Long> addNewBook(@RequestBody BookDto bookDto){
        return Response.newSuccess(bookservice.addNewBook(bookDto));
    }
    @DeleteMapping("/Book/{id}")
    public void deleteBookById(@PathVariable long id){
        bookservice.deleteBookById(id);
    }
    @PutMapping("/Book/{id}")
    public Response<BookDto> updateBookById(@PathVariable long id, @RequestParam(required = false) String name,
                                            @RequestParam double price){
        return Response.newSuccess(bookservice.updateBookById(id, name, price));
    }

}
