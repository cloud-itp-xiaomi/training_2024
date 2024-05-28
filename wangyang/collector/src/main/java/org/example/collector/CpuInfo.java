package org.example.collector;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * ClassName: CPUInfo
 * Package: com.xiaomi.work1.collector
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/26 16:08
 * @Version 1.0
 */
public class CpuInfo {

    /**
     * 返回当前主机的CPU使用率
     * 读取/proc/stat文件，读取cpu空闲时间idle和cpu总时间cpuTotal
     * 取样两次
     * cpu利用率=1-（idle2-idle1)/(cpu2-cpu1)
     * @return
     */
    //上一次查询cpu使用率
    private Long[] lastCpuTime;
    public Long[] getCpuTime() {
        long cpuTotal=0;
        long idle=0;
        String[] res=null;
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"))){

            String line = reader.readLine();
            if(line != null && line.startsWith("cpu ")){
                res = line.trim().split("\\s+");
            }
            if (res != null) {
                for(int i=1;i<res.length;i++){
                    cpuTotal+=Long.parseLong(res[i]);
                }
                idle=Long.parseLong(res[4]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Long[]{cpuTotal,idle};
    }
    public double getcpuUse() throws InterruptedException {
        //得到当前cpu时间
        Long[] time = getCpuTime();
        //如果是第一次调用，则返回0.0使用率
        if(lastCpuTime==null){
            lastCpuTime=time;
            return 0.0;
        }
        //总空闲时间
        long idle=time[1]-lastCpuTime[1];
        //总cpu时间
        long total=time[0]-lastCpuTime[0];
        lastCpuTime=time;
        return 1-idle/(double)total;
    }
}
