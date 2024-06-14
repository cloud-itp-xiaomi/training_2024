package org.qiaojingjing.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    private String hostname;
    private String file;
    private String[] logs;
}