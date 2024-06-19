package org.example.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.BaseException;
import org.example.pojo.entity.LogConfigEntity;

/**
 * 读取config.json配置文件工具类
 *
 * @author liuhaifeng
 * @date 2024/06/10/2:05
 */
@Slf4j
public class JSONParseUtil {

    public static LogConfigEntity parseJSONFile(String filePath) {
        log.info("读取配置文件：{}", filePath);
        if (filePath == null) {
            throw new BaseException("配置文件地址为空");
        }
        String jsonStr = ResourceUtil.readUtf8Str(filePath);
        LogConfigEntity logConfigEntity = JSON.parseObject(jsonStr, LogConfigEntity.class);
        return logConfigEntity;
    }
}
