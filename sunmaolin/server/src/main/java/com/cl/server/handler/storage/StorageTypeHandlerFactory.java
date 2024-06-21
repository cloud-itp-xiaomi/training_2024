package com.cl.server.handler.storage;

import com.cl.server.enums.StorageTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储类型工厂
 *
 * @author: tressures
 * @date: 2024/6/5
 */
@Component
public class StorageTypeHandlerFactory implements InitializingBean {

    @Resource
    private List<StorageTypeHandler> storageTypeHandlerList;

    private Map<StorageTypeEnum,StorageTypeHandler> handlerMap = new HashMap<>();

    public StorageTypeHandler getHandler(String type){
        StorageTypeEnum storageTypeEnum = StorageTypeEnum.getByDesc(type);
        return handlerMap.get(storageTypeEnum);
    }

    @Override
    public void afterPropertiesSet(){
        for (StorageTypeHandler subjectTypeHandler : storageTypeHandlerList) {
            handlerMap.put(subjectTypeHandler.getHandlerType(), subjectTypeHandler);
        }
    }
}
