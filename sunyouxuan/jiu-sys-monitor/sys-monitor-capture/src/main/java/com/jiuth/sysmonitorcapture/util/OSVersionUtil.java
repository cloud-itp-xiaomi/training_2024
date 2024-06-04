package com.jiuth.sysmonitorcapture.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSVersionUtil {

    public static String getLinuxDistribution() {
        String distribution = "Unknown";
        BufferedReader reader = null;

        try {
            Process process = Runtime.getRuntime().exec("lsb_release -d");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Description:")) {
                    distribution = line.substring(line.indexOf(':') + 1).trim();
                    break;
                }
            }

            process.waitFor(); // Wait for the command to finish execution
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return distribution;
    }

    public static String getLinuxVersion() {
        String version = "Unknown";
        String distributionInfo = getLinuxDistribution();

        // Try to extract version number from distribution info
        if (!distributionInfo.equals("Unknown")) {
            if (distributionInfo.contains("Ubuntu")) {
                version = distributionInfo.substring(distributionInfo.indexOf(' ') + 1);
            } else if (distributionInfo.contains("Fedora")) {
                version = distributionInfo.substring(distributionInfo.indexOf(' ') + 1, distributionInfo.lastIndexOf(' '));
            } // Add more conditions for other distributions if needed
        }

        return version;
    }

    public static void main(String[] args) {
        String distribution = getLinuxDistribution();
        String version = getLinuxVersion();

        System.out.println("Linux Distribution: " + distribution);
        System.out.println("Linux Version: " + version);
    }
}