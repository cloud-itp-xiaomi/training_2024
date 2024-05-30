package com.xiaomi2024.txh.dubbo.spring.consumer;

import com.xiaomi2024.txh.dubbo.spring.MysqlTestService;
import com.xiaomi2024.txh.dubbo.spring.consumer.api.CommonResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author txh
 * @date 2024/5/28 16:30
 */
@RestController
@RequestMapping(value = "/api/mysql_test/book_manage")
public class MysqlTestController {
    @DubboReference
    private MysqlTestService mysqlTestService;

    @GetMapping("/createTable")
    public CommonResult<?> createTable(@RequestParam(value = "table_name") String tableName) {
        mysqlTestService.createTable(tableName);
        return CommonResult.success(null,"创建表"+tableName+"成功！");
    }
    @GetMapping("/query_all_book")
    public CommonResult<?> queryAllBook(){
        List<Map<String,Object>> list = mysqlTestService.queryAllBook();
        return CommonResult.success(list,"查询成功！");
    }

    @GetMapping("/query_book_by_id")
    public CommonResult<?> queryBookById(@RequestParam(value = "id") int id){
        Map<String,Object> book = mysqlTestService.queryBookById(id);
        return CommonResult.success(book,"查询成功！");
    }

    @GetMapping("/query_book_by_name")
    public CommonResult<?> queryBookByName(@RequestParam(value = "name") String name){
        Map<String,Object> book = mysqlTestService.queryBookByName(name);
        return CommonResult.success(book,"查询成功！");
    }

    @GetMapping("/add_book")
    public CommonResult<?> addBook(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "author") String author,
                                   @RequestParam(value = "price") int price){
        String result = mysqlTestService.addBook(name, author, price);
        return CommonResult.success(result);
    }

    @GetMapping("/update_book")
    public CommonResult<?> updateBook(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "price") int price){
        String result = mysqlTestService.updateBook(name, price);
        return CommonResult.success(result);
    }

    @GetMapping("/delete_book")
    public CommonResult<?> deleteBook(@RequestParam(value = "name") String name){
        String result = mysqlTestService.deleteBook(name);
        return CommonResult.success(result);
    }

    @GetMapping("/set_top10_books_in_redis")
    public CommonResult<?> setTop10BooksInRedis(){
        String result = mysqlTestService.setTop10BooksInRedis();
        return CommonResult.success(result);
    }

    @GetMapping("/get_top10_books_in_redis")
    public CommonResult<?> getTop10BooksInRedis(){
        List<Object> list= mysqlTestService.queryTop10Books();
        return CommonResult.success(list, "检索成功！");
    }

    @GetMapping("/del_key_in_redis")
    public CommonResult<?> delKeyInRedis(@RequestParam(value = "key") String key){
        String rs= mysqlTestService.delKey(key);
        return CommonResult.success(rs);
    }

}
