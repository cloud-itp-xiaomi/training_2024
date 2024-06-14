package com.jiuth.sysmonitorcapture;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"})
class SysMonitorCaptureApplicationTests {
    @Test
    void contextLoads() {
    }


}
