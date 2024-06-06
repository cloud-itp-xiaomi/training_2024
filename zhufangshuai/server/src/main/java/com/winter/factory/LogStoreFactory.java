package com.winter.factory;

/**
 * 日志存储的工厂
 * */
public class LogStoreFactory {

    /**
     * 根据传入的参数，判断具体的存储方式
     * */
    public static LogStore getStorageMethod(String storage){
        LogStore logStore = null;
        if (storage.equalsIgnoreCase("local_file")){
            logStore = new LocalFileStore();
        } else if (storage.equalsIgnoreCase("mysql")){
            logStore = new MySQLStore();
        } else if (storage.equalsIgnoreCase("elasticsearch")) {
            logStore = new ESStore();
        }
        return logStore;
    }
}
