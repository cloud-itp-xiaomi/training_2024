package org.xiaom.yhl.collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: SshProperties
 * Package: org.xiaom.yhl.collector.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/30 13:50
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "collector.ssh")
public class SshProperties {
    private String host;
    private String user;
    private String password;
    private int port;

    //使用IDEA的生成功能生成get，set方法

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
