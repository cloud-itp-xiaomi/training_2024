package com.example.springcloud.base;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @ClassName IdTest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-05 11:11
 **/
@SpringBootTest(classes = {com.example.springcloud.ServerApplication.class})
@RunWith(SpringRunner.class)
public class BaseTest {
    @Value("${server.port}")
    private Long workerId;
    @Autowired
    private SnowflakeIdGenerator idGenerator;
    @Test
    public void idTest() {
        for (int i = 0; i < 100; i++) {
            long id = idGenerator.nextId();
            System.out.println(id);
        }

    }
    @Test
    public void idTest2() {
        System.out.println(workerId);
    }


}
