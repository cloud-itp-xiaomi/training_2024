package org.example.config.nacos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * nacos 配置信息
 *
 * @author liuhaifeng
 * @date 2024/06/15/18:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NacosConfigInfo {

    private String serverAddr;

    private String namespace;

    private String group;

    private String dataId;

    private Boolean refresh = true;

    private Class cls = String.class;

    public long getTimeout() {
        return 5000L;
    }
}
