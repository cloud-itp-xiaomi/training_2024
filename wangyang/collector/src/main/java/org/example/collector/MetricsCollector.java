package org.example.collector;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassName: MetricsCollerctor
 * Package: com.xiaomi.work1.collector
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/26 16:09
 * @Version 1.0
 */
public class MetricsCollector {
    private final static String SERVER_URL="http://172.27.72.128:8080/api/metric/upload";

    public static void main(String[] args) {
        //创建定时任务定期收集信息并且向服务器发送请求
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    collectMetricsInfo();
                }catch (Exception e){
                    throw new RuntimeException(e);
                }

            }
        },0,60000);
    }
    public static void collectMetricsInfo() throws Exception {
        CpuInfo cpuInfo = new CpuInfo();
        MemInfo memInfo = new MemInfo();
        //获取信息并封装到json对象
        double cpuUse=cpuInfo.getcpuUse();
        double memUse=memInfo.getmemUse();
        String ip = InetAddress.getLocalHost().getHostAddress();

        JSONObject cpuJson = new JSONObject();
        cpuJson.put("metric","cpu.used.percent");
        cpuJson.put("endpoint",ip);
        cpuJson.put("timestamp",System.currentTimeMillis());
        cpuJson.put("step",60);
        cpuJson.put("value",cpuUse);

        JSONObject memJson = new JSONObject();
        memJson.put("metric","mem.used.percent");
        memJson.put("endpoint",ip);
        memJson.put("timestamp",System.currentTimeMillis());
        memJson.put("step",60);
        memJson.put("value",memUse);

        //将获取到的信息装入JSON数组
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(cpuJson);
        jsonArray.add(memJson);
        //将json数组发送到服务器
        sendPost(jsonArray);

    }
    public static void sendPost(JSONArray metrics) throws IOException {
        // 创建url资源
        URL url = new URL(SERVER_URL);
        // 建立http连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置允许输出
        conn.setDoOutput(true);
        // 设置允许输入
        conn.setDoInput(true);
        // 设置传递方式
        conn.setRequestMethod("POST");
        // 设置维持长连接
        conn.setRequestProperty("Connection", "Keep-Alive");
        // 设置文件字符集:
        conn.setRequestProperty("Charset", "UTF-8");
        // 转换为字节数组
        byte[] data = metrics.toString().getBytes();
        // 设置文件长度
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        // 设置文件类型:
        conn.setRequestProperty("Content-Type", "application/json");
        // 开始连接请求
        conn.connect();
        OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
        // 写入请求的字符串
        out.write(data);
        out.flush();
        //关闭连接
        out.close();
        conn.disconnect();
    }
}
