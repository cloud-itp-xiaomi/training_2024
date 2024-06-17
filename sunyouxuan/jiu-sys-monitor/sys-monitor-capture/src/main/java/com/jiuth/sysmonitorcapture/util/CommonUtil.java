package com.jiuth.sysmonitorcapture.util;

public class CommonUtil {
    public static String initializeEndpoint(String serverUrl) {
        String username = System.getProperty("user.name", "UnknownUser");
        String systemVersion = OSVersionUtil.getSystemVersion();
        String ipAddress = null;
        if (serverUrl != null && (serverUrl.contains("127.0.0.1") || serverUrl.contains("http://localhost"))) {
            ipAddress = "127.0.0.1";
        } else {
            ipAddress = IpUtil.getLocalIpAddress();
        }

        return username + "@" + systemVersion + "@" + ipAddress;
    }

    public static void main(String[] args) {
        String endpoint = initializeEndpoint("http://localhost:8080");
        System.out.println("System Endpoint: " + endpoint);
    }
}
