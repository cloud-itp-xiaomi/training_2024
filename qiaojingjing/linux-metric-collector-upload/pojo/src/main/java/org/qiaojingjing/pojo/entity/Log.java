package org.qiaojingjing.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private Long Id;
    private String hostname;
    private String file;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
