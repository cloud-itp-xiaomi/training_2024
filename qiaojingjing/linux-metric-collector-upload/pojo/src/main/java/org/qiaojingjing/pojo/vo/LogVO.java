package org.qiaojingjing.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LogVO {
    private String hostname;
    private String file;
    private List<String> logs;
}
