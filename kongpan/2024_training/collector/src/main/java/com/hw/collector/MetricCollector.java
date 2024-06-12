package com.hw.collector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hw.collector.watcher.CPUWatcher;
import com.hw.collector.watcher.MemoryWatcher;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author mrk
 * @create 2024-05-21-15:30
 */
public class MetricCollector {

    /**
     * 定时请求上传接口
     */
    private static final String SERVER_URL = "http://192.168.80.1:8080/api/metric/upload";

    /**
     * 指标的采集周期
     */
    private static final int INTERVAL = 60000;

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    collectAndSendMetrics();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, INTERVAL);
    }

    private static void collectAndSendMetrics() throws IOException {
        CPUWatcher cpuWatcher = new CPUWatcher();
        MemoryWatcher memoryWatcher = new MemoryWatcher();
        double cpuUsage = formatUsage(cpuWatcher.getCpuUsage());
        double memoryUsage = formatUsage(memoryWatcher.getMemoryUsage());

        /*InetAddress inetAddress = InetAddress.getLocalHost();
        String ip = inetAddress.getHostAddress();*/
        String ip = getIpAddress();

        JSONArray metrics = new JSONArray();

        JSONObject cpuMetric = new JSONObject();
        cpuMetric.put("metric", "cpu.used.percent");
        cpuMetric.put("endpoint", ip);
        cpuMetric.put("timestamp", System.currentTimeMillis() / 1000);
        cpuMetric.put("step", 60);
        cpuMetric.put("value", cpuUsage);

        JSONObject memMetric = new JSONObject();
        memMetric.put("metric", "mem.used.percent");
        memMetric.put("endpoint", ip);
        memMetric.put("timestamp", System.currentTimeMillis() / 1000);
        memMetric.put("step", 60);
        memMetric.put("value", memoryUsage);

        metrics.add(cpuMetric);
        metrics.add(memMetric);

        sendMetrics(metrics);
        System.out.println("cpu usage: " + cpuUsage + " memory usage: " + memoryUsage);
    }

    private static void sendMetrics(JSONArray metrics) throws IOException {
        URL url = new URL(SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(metrics.toString().getBytes());
        outputStream.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed! HTTP error code : " + connection.getResponseCode());
        }

        connection.disconnect();
    }

    private static double formatUsage(double number) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);
        String str = numberFormat.format(number);
        return Double.parseDouble(str);
    }

    private static String getIpAddress() {
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
                        if (ip != null && ip instanceof Inet4Address) {
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
}
