package com.jiuth.sysmonitorcapture.storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author jiuth
 */
@Component
public class LogStorageFactory {

    private static ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        LogStorageFactory.context = context;
    }
    public LogStorageService createLogStorageService(String logStorageType) {
        return switch (logStorageType) {
            case "local_file" -> context.getBean(LocalFileLogStorageService.class);
            case "mysql" -> context.getBean(MySqlLogStorageService.class);
            default -> throw new IllegalArgumentException("Unsupported log storage type: " + logStorageType);
        };
    }
}