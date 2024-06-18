package com.lx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResult implements Serializable {

    private Integer code; //状态码
    private String message;//请求信息
    private ResLog data;//返回数据

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n").append("  code = " ).append(code).append("\n");
        sb.append("  message = \"").append(message).append("\"").append("\n");
        sb.append("  data = \"").append(message).append("\"").append("\n");
        sb.append("}");
        return sb.toString();
    }

}
