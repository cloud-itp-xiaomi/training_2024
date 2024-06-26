package com.lx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResLog implements Serializable {

    private String hostName;//主机名
    private String file;//对应日志文件
    private List<String> logs;//日志

    //为logs属性添加元素
    public void addLog(LogMysql logMysql) {
        logs.add(logMysql.getLog());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("hostName: \"").append(hostName).append('\"');
        sb.append(", file: ").append(file);
        sb.append(", logs: ").append(logs);
        sb.append('}');
        return sb.toString();
    }
}
