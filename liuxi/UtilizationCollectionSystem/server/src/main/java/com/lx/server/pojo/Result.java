package com.lx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //包含全部参数的构造函数
@NoArgsConstructor  //无参构造函数
public class Result {

    private Integer code; //状态码
    private String message;//请求信息
    private ResUtilization[] data;//返回数据

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n").append("  code = " ).append(code).append("\n");
        sb.append("  message = \"").append(message).append("\"").append("\n");
        sb.append("     [\n");
        for (ResUtilization resUtilization : data) {
            sb.append("         ").append(resUtilization).append(",\n");
        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("     ]\n").append("}");
        return sb.toString();
    }

}
