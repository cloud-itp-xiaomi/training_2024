package org.qiaojingjing.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogMysqlVO {
    private String hostname;
    private String file;
    private String logs;
}
