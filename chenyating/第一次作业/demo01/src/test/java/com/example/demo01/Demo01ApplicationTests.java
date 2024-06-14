package com.example.demo01;

import com.example.demo01.bean.DataItem;
import com.example.demo01.mapper.DataMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class Demo01ApplicationTests {

    @Test
    void contextLoads() {
    }

}
