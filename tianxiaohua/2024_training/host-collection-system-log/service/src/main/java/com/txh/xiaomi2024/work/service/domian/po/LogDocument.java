package com.txh.xiaomi2024.work.service.domian.po;

import lombok.*;

import java.util.List;
/**
 * @author txh
 * @Date 2024/6/03 10:04
 * @Desc 日志信息存储表字段信息
 */
@Data //包含了get，set和toString
@Builder
@AllArgsConstructor //有参构造器 set
@NoArgsConstructor // 用于反序列化
@EqualsAndHashCode
public class LogDocument {
    private static final long serialVersionUID = -2109499465893133270L;

    private Long id; // 日至id
    private String hostname; // 主机
    private String file; // 日志文件
    private List<String> logs; // 日志列表
    private Long last_update_time; // 最后更新时间

    public Long getId() {
        if (id == null) {
            id = IdGeneratorUtil.standAloneSnowFlake();
        }
        return id;
    }
}
