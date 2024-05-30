package com.xiaomi2024.txh.dubbo.spring;

import java.util.List;
import java.util.Map;

public interface MysqlTestService {
    String createTable(String tableName);
    List<Map<String,Object>> queryAllBook();
    String addBook(String name, String author, int price);
    String updateBook(String name, int price);
    String deleteBook(String name);
    Map<String,Object> queryBookById(int id);
    Map<String,Object> queryBookByName(String bookName);
    String setTop10BooksInRedis();
    List<Object> queryTop10Books();
    String delKey(String key);
}
