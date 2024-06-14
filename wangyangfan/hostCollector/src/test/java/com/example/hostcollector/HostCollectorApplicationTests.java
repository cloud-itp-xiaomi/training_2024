package com.example.hostcollector;

import com.example.hostcollector.utils.IOProcessUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.InetAddress;

@SpringBootTest
class HostCollectorApplicationTests {

    @Test
    void contextLoads() throws IOException {
        System.out.println(IOProcessUtils.getMemUtilization());

    }

}
