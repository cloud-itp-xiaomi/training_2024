package org.qiaojingjing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private String hostname;
    private String file;
    private List<String> logs;
}
