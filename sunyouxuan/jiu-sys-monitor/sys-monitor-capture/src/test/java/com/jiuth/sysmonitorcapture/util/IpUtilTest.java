package com.jiuth.sysmonitorcapture.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpUtilTest {

    @Test
    void getLocalIpAddress() {
        String ipAddress = IpUtil.getLocalIpAddress();
        assertNotNull(ipAddress);
        assertTrue(ipAddress.matches("\\d+\\.\\d+\\.\\d+\\.\\d+"));
        System.out.println("Local IP Address: " + ipAddress);
        // 可以添加更多的测试逻辑，例如排除特定 IP 地址段等
    }
}