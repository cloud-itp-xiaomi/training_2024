package com.example.springcloud.base;

import cn.hutool.json.JSONUtil;
import com.example.springcloud.controller.ReaderController;
import com.example.springcloud.controller.base.Response;
import com.example.springcloud.controller.response.ReaderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName BaseTest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 19:39
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
    @Autowired
    private ReaderController readerController;
    @Test
    public void test() {
        Response<ReaderResponse> readerResponse = readerController.readMsg("scm", 1, 1717598629L, 1717662780L);
        System.out.println(readerResponse.getData());
        String str = JSONUtil.toJsonStr(readerResponse);
        System.out.println(str);
    }
    @Test
    public void test1() {
        System.out.println("test");
    }
}
