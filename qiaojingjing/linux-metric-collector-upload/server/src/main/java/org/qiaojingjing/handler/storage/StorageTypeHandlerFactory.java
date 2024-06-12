package org.qiaojingjing.handler.storage;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.config.LogStorageProperties;
import org.qiaojingjing.constant.ExceptionConstant;
import org.qiaojingjing.enums.StorageTypeEnum;
import org.qiaojingjing.exception.MyIllegalParamException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 存储方式工厂类
 * 支持两种存储方式：
 * 1.本地文件
 * 2.MySQL数据库
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/

@Slf4j
@Configuration
public class StorageTypeHandlerFactory {
    private static StorageTypeHandler storageTypeHandler;
    @Resource
    private MysqlStorageHandler mysqlStorageHandler;
    @Resource
    private LocalFileStorageHandler localFileStorageHandler;
    @Resource
    private LogStorageProperties logStorageProperties;

    @PostConstruct
    public void init(){
        String storageType = logStorageProperties.getType();
        StorageTypeEnum storageTypeIgnoreCase = StorageTypeEnum.fromType(storageType);
        switch (storageTypeIgnoreCase){
            case LOCAL_FILE:
                storageTypeHandler = localFileStorageHandler;
                break;
            case MYSQL:
                storageTypeHandler = mysqlStorageHandler;
                break;
            default:
                log.error("不支持的存储类型:{}",storageType);
                throw new MyIllegalParamException(ExceptionConstant.ILLEGAL_TYPE);
        }
    }

    public static StorageTypeHandler getStorageHandler(){
        return storageTypeHandler;
    }
}
