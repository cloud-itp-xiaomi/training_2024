package org.example.handler;

import org.example.enums.LogSaveTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志保存处理器工厂
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:29
 */
public class LogSaveHandlerFactory {

    private static final Map<LogSaveTypeEnum, AbstractLogSaveHandler> LOG_SAVE_HANDLER_MAP = new HashMap<>();

    public static void register(LogSaveTypeEnum logSaveTypeEnum, AbstractLogSaveHandler logSaveHandler) {
        LOG_SAVE_HANDLER_MAP.put(logSaveTypeEnum, logSaveHandler);
    }

    public static AbstractLogSaveHandler getHandler(LogSaveTypeEnum logSaveTypeEnum) {
        return LOG_SAVE_HANDLER_MAP.get(logSaveTypeEnum);
    }
}
