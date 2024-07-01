package org.qiaojingjing.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Slf4j
public class LogConfig {
    private static final String CONFIG_PATH = "config.json";

    public static List<String> getFilesPathList() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassLoader classLoader = LogConfig.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(CONFIG_PATH);
            if (inputStream == null) {
                log.error("配置文件{}未找到",CONFIG_PATH);
            }
            Map<String, List<String>> map = objectMapper.readValue(new InputStreamReader(inputStream), new TypeReference<>() {
            });
            return map.get("files");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
