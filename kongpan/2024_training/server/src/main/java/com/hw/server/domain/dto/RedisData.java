package com.hw.server.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author mrk
 * @create 2024-06-02-16:19
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
