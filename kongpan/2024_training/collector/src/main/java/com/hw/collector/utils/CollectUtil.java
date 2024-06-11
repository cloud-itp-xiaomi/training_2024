package com.hw.collector.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author mrk
 * @create 2024-06-06-19:24
 */
@Component
public class CollectUtil {

    public String getIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        ip = inetAddresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("Unable to get IP address!!!");
        }
        return "my-computer";
    }

    public JSONObject parseJsonFile(String path) throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new IOException("Config read failed!");
        } else {
            return JSON.parseObject(resourceAsStream, JSONObject.class);
        }
    }

    public String getLogStorageType(String path) throws IOException {
        JSONObject parsedJsonFile = parseJsonFile(path);
        return (String) parsedJsonFile.get("log_storage");
    }

    public String[] getLogFilePaths(String path) throws IOException {
        JSONObject parsedJsonFile = parseJsonFile(path);
        JSONArray fileArray = (JSONArray) parsedJsonFile.get("files");
        String[] files = new String[fileArray.size()];
        return fileArray.toArray(files);
    }

}
