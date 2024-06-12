package org.qiaojingjing.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum StorageTypeEnum {
    LOCAL_FILE("local_file"),
    MYSQL("mysql");
    private final String type;

    StorageTypeEnum(String type) {
        this.type = type;
    }

    public static StorageTypeEnum fromType(String type){
        for (StorageTypeEnum storageType  : values()) {
            if(storageType.type.equalsIgnoreCase(type)){
                return storageType;
            }
        }
        log.error("没有"+type+"类型的存储方式!已采用默认存储方式！");
        return StorageTypeEnum.LOCAL_FILE;
    }
}
