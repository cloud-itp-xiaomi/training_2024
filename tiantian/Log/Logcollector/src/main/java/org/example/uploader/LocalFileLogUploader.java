package org.example.uploader;

import org.example.common.Result;
import org.example.entity.LogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component("localFileLogUploader")
public class LocalFileLogUploader implements LogUploader {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${upload.local.url}")  // 从配置文件注入URL
  private String serverUrl;

  @Override
  public void uploadFiles(List<LogFile> logFiles) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<List<LogFile>> request = new HttpEntity<>(logFiles, headers);
    ResponseEntity<Result> response = restTemplate.postForEntity(serverUrl, request, Result.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      System.out.println("Logs uploaded successfully to server on local file.");
    } else {
      System.out.println("Failed to upload logs to server on local file.");
    }
  }
}
