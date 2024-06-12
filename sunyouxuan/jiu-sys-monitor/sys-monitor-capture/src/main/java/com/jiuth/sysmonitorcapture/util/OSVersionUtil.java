package com.jiuth.sysmonitorcapture.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OSVersionUtil {

    public static String getSystemVersion() {
        String version = "Unknown";

        try (BufferedReader reader = new BufferedReader(new FileReader("/etc/os-release"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PRETTY_NAME=")) {
                    version = line.substring(line.indexOf('=') + 1).trim().replaceAll("\"", "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

                // 去除版本号中的空格
        version = version.replace(" ", "");
        return version;
    }

    public static void main(String[] args) {
        String version = getSystemVersion();
        System.out.println("System Version: " + version);
    }
}