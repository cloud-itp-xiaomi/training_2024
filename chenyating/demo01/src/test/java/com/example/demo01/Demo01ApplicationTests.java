package com.example.demo01;

import com.example.demo01.bean.DataItem;
import com.example.demo01.mapper.DataMapper;
import com.example.demo01.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class Demo01ApplicationTests {
    @Autowired
    DataMapper dataMapper;
    DataService dataService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test_insert() {
        DataItem user = new DataItem("cpu.used.percent", "my-computer", 1715765640, 60, 60.1);
        dataMapper.insert(user);
        System.out.println(dataMapper.getAll());
    }
}
