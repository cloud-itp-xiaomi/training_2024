package com.xiaomi2024.txh.dubbo.spring.provider;

import com.xiaomi2024.txh.dubbo.spring.provider.service.RedisService;
import org.apache.dubbo.config.annotation.DubboService;
import com.xiaomi2024.txh.dubbo.spring.MysqlTestService;
import com.xiaomi2024.txh.dubbo.spring.provider.bean.Book;
import com.xiaomi2024.txh.dubbo.spring.provider.dao.BookManageDao;

import java.util.*;
@DubboService// 通过这个配置可以基于 Spring Boot 去发布 Dubbo 服务
public class MysqlTestServiceImpl implements MysqlTestService {
    private final BookManageDao bookManageDao;
    private final RedisService redisService;

    public MysqlTestServiceImpl(BookManageDao bookManageDao, RedisService redisService) {
        this.bookManageDao = bookManageDao;
        this.redisService = redisService;
    }

    @Override
    public String createTable(String tableName) {
        bookManageDao.createTable(tableName);
        return "已创建表："+tableName;
    }

    @Override
    public List<Map<String,Object>> queryAllBook() {
        List<Book> list = bookManageDao.queryAllBook();
        List<Map<String,Object>> books = new ArrayList<>();
        for(Book book:list){
            Map<String ,Object> map = new HashMap<>();
            map.put("id",book.getId());
            map.put("name",book.getName());
            map.put("author",book.getAuthor());
            map.put("price",book.getPrice());
            books.add(map);
        }
        return books;
    }

    @Override
    public String addBook(String name, String author, int price) {
        Book book = new Book(name,author,price);
        bookManageDao.addBook(book);
        return "添加图书["+name+"]成功";
    }

    @Override
    public String updateBook(String name, int price) {
        Book book = bookManageDao.queryBookByName(name);
        book.setPrice(price);
        bookManageDao.updateBook(book);
        return "修改图书["+name+"]的价格为"+price;
    }

    @Override
    public String deleteBook(String name) {
        bookManageDao.deleteBook(name);
        return "删除图书["+name+"]成功";
    }

    @Override
    public Map<String,Object> queryBookById(int id) {
        Book book = bookManageDao.queryBookById(id);
        Map<String ,Object> map = new HashMap<>();
        map.put("id",book.getId());
        map.put("name",book.getName());
        map.put("author",book.getAuthor());
        map.put("price",book.getPrice());
        return map;
    }

    @Override
    public Map<String,Object> queryBookByName(String bookName) {
        Book book = bookManageDao.queryBookByName(bookName);
        Map<String ,Object> map = new HashMap<>();
        map.put("id",book.getId());
        map.put("name",book.getName());
        map.put("author",book.getAuthor());
        map.put("price",book.getPrice());
        return map;
    }

    @Override
    public String setTop10BooksInRedis() {
        List<Map<String,Object>> list = queryAllBook();
        List<Map<String,Object>> result = new ArrayList<>();
        int j = 0;
        for (Map<String,Object> map : list) {
            if(j == 10)break;
            result.add(map);
            j++;
        }
        redisService.lPush("books",result);
        return "前10条数据已经存入redis";
    }

    @Override
    public List<Object> queryTop10Books() {
        List<Object> list = redisService.lRange("books",0, redisService.lSize("books"));
        return list;
    }

    @Override
    public String delKey(String key) {
        redisService.del("books");
        return "删除key "+key+ " 成功！";
    }
}
