package com.jiuth.sysmonitorcapture.util;

import org.junit.jupiter.api.Test;

public class OSVersionUtilTest {

    @Test
    public void OSVersionUtilTest() {
        String distribution = OSVersionUtil.getSystemVersion();
        System.out.println("System Distribution:"+distribution);

    }
}
