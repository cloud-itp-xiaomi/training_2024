package com.hw.collector.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.hw.collector.dto.ConfigDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * @author mrk
 * @create 2024-06-17-20:22
 */
@Component
@RequiredArgsConstructor
public class NacosJsonConfig {

    private final NacosConfigManager nacosConfigManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String DATA_ID = "cfg.json";
    private static final String GROUP = "DEFAULT_GROUP";
    private static final long TIMEOUT = 3000L;

    private ConfigDTO configDTO;

    @PostConstruct
    public void init() {
        try {
            ConfigService configService = nacosConfigManager.getConfigService();
            String configInfo = configService.getConfig(DATA_ID, GROUP, TIMEOUT);
            updateConfig(configInfo);

            configService.addListener(DATA_ID, GROUP, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    updateConfig(s);
                }
            });
        } catch (NacosException e) {
            logger.error("Error fetching config from Nacos");
        }
    }

    private void updateConfig(String configInfo) {
        this.configDTO = JSON.parseObject(configInfo, ConfigDTO.class);
        logger.info("Updated config: {}", this.configDTO);
    }

    public ConfigDTO getConfigDTO() {
        return this.configDTO;
    }
}
