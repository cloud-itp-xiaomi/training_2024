package org.example.web;

import org.example.common.api.CommonResult;
import org.example.mysqltest.bean.Book;
import org.example.mysqltest.service.BookManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author txh
 * @date 2024/5/26 16:30
 */
@RestController
@RequestMapping(value = "/api/mysql_test/book_manage")
public class MysqlTestController {
    private final BookManageService bookManageService;

    @Autowired
    public MysqlTestController(BookManageService bookManageService) {
        this.bookManageService = bookManageService;
    }

    @GetMapping("/createTable")
    public CommonResult<?> createTable(@RequestParam(value = "table_name") String tableName) {
        bookManageService.createTable(tableName);
        return CommonResult.success(null,"创建表成功！");
    }
    @GetMapping("/query_all_book")
    public CommonResult<?> queryAllBook(){
        List<Book> list = bookManageService.queryAllBook();
        return CommonResult.success(list,"查询成功！");
    }

    @GetMapping("/query_book_by_id")
    public CommonResult<?> queryBookById(@RequestParam(value = "id") int id){
        Book book = bookManageService.queryBookById(id);
        return CommonResult.success(book,"查询成功！");
    }

    @GetMapping("/query_book_by_name")
    public CommonResult<?> queryBookByName(@RequestParam(value = "name") String name){
        Book book = bookManageService.queryBookByName(name);
        return CommonResult.success(book,"查询成功！");
    }

    @GetMapping("/add_book")
    public CommonResult<?> addBook(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "author") String author){
        Book book = new Book(name,author);
        boolean result = bookManageService.addBook(book);
        if(!result) return CommonResult.failed("插入数据失败");
        return CommonResult.success("插入数据成功！");
    }

    @GetMapping("/update_book")
    public CommonResult<?> updateBook(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "author") String author,
                                      @RequestParam(value = "id") int id){
        Book book = bookManageService.queryBookById(id);
        book.setName(name);
        book.setAuthor(author);
        boolean result = bookManageService.updateBook(book);
        if(!result) return CommonResult.failed("更新数据失败");
        return CommonResult.success("更新数据成功！");
    }

    @GetMapping("/delete_book")
    public CommonResult<?> deleteBook(@RequestParam(value = "id") int id){
        boolean result = bookManageService.deleteBook(id);
        if(!result) return CommonResult.failed("删除数据失败");
        return CommonResult.success("删除数据成功！");
    }

}
