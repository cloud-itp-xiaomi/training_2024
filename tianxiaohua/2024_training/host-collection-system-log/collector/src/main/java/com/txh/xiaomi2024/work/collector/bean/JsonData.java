package com.txh.xiaomi2024.work.collector.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * @author txh
 * @Date 2024/6/03 10:20
 */
@Data // 包含了get，set和toString
@AllArgsConstructor // 有参构造器 set
@NoArgsConstructor // 用于反序列化
public class JsonData {
    private List<String> files;
    private String log_storage;
}
