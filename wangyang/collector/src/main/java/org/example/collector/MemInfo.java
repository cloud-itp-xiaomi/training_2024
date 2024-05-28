package org.example.collector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * ClassName: MemInfo
 * Package: com.xiaomi.work1.collector
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/26 16:08
 * @Version 1.0
 */
public class MemInfo {
    /**
     * 返回当前主机的内存使用率
     * 1-空闲内存/主内存
     * @return
     */
    public double getmemUse() {
        String line=null;
        long freeMem=0;
        long totalMem=0;
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"))){

            while ((line=reader.readLine())!=null){
                if(line.startsWith("MemTotal:")){
                    totalMem=parseLine(line);
                }else if(line.startsWith("MemFree:")){
                    freeMem=parseLine(line);
                }
                if(totalMem>0&&freeMem>0){
                    break;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1.0-freeMem/(double)totalMem;
    }

    private long parseLine(String line) {
        String[] res = line.trim().split("\\s+");
        return Long.parseLong(res[1]);
    }

}
