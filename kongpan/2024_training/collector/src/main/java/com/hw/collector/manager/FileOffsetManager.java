package com.hw.collector.manager;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.hw.collector.constants.RedisConstants.CACHE_OFFSET_KEY_PREFIX;

/**
 * @author mrk
 * @create 2024-06-13-23:13
 */
@Component
@RequiredArgsConstructor
public class FileOffsetManager {

    private final StringRedisTemplate stringRedisTemplate;
//    private Map<String, Long> offsetMap = new HashMap<>();

    public long getOffset(String filePath, String storageType) {
        String offset = stringRedisTemplate.opsForValue()
                .get(CACHE_OFFSET_KEY_PREFIX + storageType + filePath);
        return StrUtil.isNotBlank(offset) ? Long.parseLong(offset) : 0L;
    }

    public void setOffset(String filePath, String storageType, long offset) {
        stringRedisTemplate.opsForValue()
                .set(CACHE_OFFSET_KEY_PREFIX + storageType + filePath, String.valueOf(offset));
    }
}
