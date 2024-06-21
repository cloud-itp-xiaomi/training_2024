package org.example.uploader;

import org.example.entity.LogFile;
import java.util.List;

//接口类，定义uploadFiles
public interface LogUploader {
  void uploadFiles(List<LogFile> logFiles);
}
