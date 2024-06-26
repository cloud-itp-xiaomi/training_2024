package com.collector.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonTests {
    Common common = new Common();

    @Test
    void getHostName() {
        System.out.println(common.getHostName());
    }

    @Test
    void getSystemType() {
        System.out.println(common.getSystemType());
    }
}