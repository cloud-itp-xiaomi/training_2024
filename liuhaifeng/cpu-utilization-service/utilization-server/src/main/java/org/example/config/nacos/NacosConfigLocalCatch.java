package org.example.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NacosConfigLocalCatch{

    private static ThreadPoolExecutor threadPoolExecutor;

    protected final String clazzSimpleName = getClass().getSimpleName();

    private Map<String, Object> localCatchMap = new HashMap<>();

    @Resource(name = "nacosConfigLocalCacheInfoMap")
    private Map<String, Class> nacosConfigLocalCacheInfoMap;

    @Value("${utilization-server.config.file-path}")
    private String configFilePath;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @PostConstruct
    public void init() {
        nacosConfigLocalCacheInfoMap.forEach((k, v) -> {
            NacosConfigInfo nacosConfigInfo = new NacosConfigInfo(nacosConfigProperties.getServerAddr(),
                    nacosConfigProperties.getNamespace(), nacosConfigProperties.getGroup(),
                    k, true, nacosConfigLocalCacheInfoMap.get(k));
            this.listener(nacosConfigInfo);
        });
        threadPoolExecutor = new ThreadPoolExecutor(
                2, 4, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private void listener(NacosConfigInfo nacosConfigInfo) {
        // 配置信息监听器，当配置发生变化时会收到通知
        Listener listener = new Listener() {
            @Override
            public Executor getExecutor() {
                return threadPoolExecutor;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("{}#@PostConstruct receive configInfo. configInfo={}", clazzSimpleName, configInfo);
                compile(configInfo, nacosConfigInfo);
            }
        };
        ConfigService configService = this.getConfigService(nacosConfigInfo);
        String config;
        try {
            config = configService.getConfig(nacosConfigInfo.getDataId(), nacosConfigInfo.getGroup(), nacosConfigInfo.getTimeout());
            log.info("{}#afterPropertiesSet init configInfo. configInfo={}", clazzSimpleName, config);
            // 初始化
            compile(config, nacosConfigInfo);
            // 监听
            configService.addListener(nacosConfigInfo.getDataId(), nacosConfigInfo.getGroup(), listener);
        } catch (NacosException e) {
            e.printStackTrace();
            throw new RuntimeException("nacos server 监听 异常! dataId = " + nacosConfigInfo.getDataId());
        }
    }

    private void compile(String config, NacosConfigInfo nacosConfigInfo) {
        Object initValue = JSON.parseObject(config, nacosConfigInfo.getCls());
        log.info("读取到nacos配置,{}", config);
        writeConfigFile(config);
        localCatchMap.put(nacosConfigInfo.getDataId(), initValue);
    }

    private void writeConfigFile(String config) {
        try {
            File file = new ClassPathResource(configFilePath).getFile();
            OutputStream outputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.println(config);
            outputStream.close();
            printWriter.close();
        } catch (IOException e) {
            throw new BaseException("nacos配置本地化出错");
        }
    }

    /**
     * 获取ConfigService
     *
     * @param nacosConfigInfo NacosConfigInfo
     * @return configService
     */
    private ConfigService getConfigService(NacosConfigInfo nacosConfigInfo) {
        String serverAddr = nacosConfigInfo.getServerAddr();
        String nameSpace = nacosConfigInfo.getNamespace();
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        if (!StringUtils.isEmpty(nameSpace)) {
            properties.put(PropertyKeyConst.NAMESPACE, nameSpace);
        }
        ConfigService configService;
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
            throw new RuntimeException("Nacos config 配置 异常");
        }
        return configService;
    }

    public <T> T get(String dataId, Class<T> cls) {
        if (cls != nacosConfigLocalCacheInfoMap.get(dataId)) {
            throw new IllegalArgumentException("类型异常");
        }
        return (T) localCatchMap.get(dataId);
    }
}
