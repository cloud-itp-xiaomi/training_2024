package org.example.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.LogConfigEntity;

import java.io.File;
import java.io.FileReader;

/**
 * 读取config.json配置文件工具类
 *
 * @author liuhaifeng
 * @date 2024/06/10/2:05
 */
@Slf4j
public class JSONParseUtil {

    public static LogConfigEntity parseJSONFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("文件不存在");
        }
        String jsonStr = readFile(file);
        LogConfigEntity logConfigEntity = JSON.parseObject(jsonStr, LogConfigEntity.class);
        return logConfigEntity;
    }

    private static String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(file)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
        } catch (Exception e) {
            log.error("读取文件失败");
            e.printStackTrace();
        }
        return content.toString();
    }
}
