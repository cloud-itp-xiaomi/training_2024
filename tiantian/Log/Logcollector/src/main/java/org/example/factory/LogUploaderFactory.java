package org.example.factory;

import org.example.uploader.LogUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LogUploaderFactory {
  private final ApplicationContext context;

  @Autowired
  public LogUploaderFactory(ApplicationContext context) {
    this.context = context;
  }

  // 根据type创LogUploader
  public LogUploader createLogUploader(String type) {
    switch (type) {
      case "server":
        return context.getBean("serverLogUploader", LogUploader.class);
      case "local":
        return context.getBean("localFileLogUploader", LogUploader.class);
      default:
        throw new IllegalArgumentException("Unknown uploader type: " + type);
    }
  }
}
