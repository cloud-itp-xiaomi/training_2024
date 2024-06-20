package org.example.pojo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志配置实体类
 *
 * @author liuhaifeng
 * @date 2024/06/10/2:57
 */
@Data
public class LogConfigEntity {

    private List<String> files;

    @JSONField(name = "log_storage")
    private String logStorage;
}
