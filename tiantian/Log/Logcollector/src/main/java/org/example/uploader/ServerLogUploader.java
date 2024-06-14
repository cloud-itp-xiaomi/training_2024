package org.example.uploader;

import org.example.common.Result;
import org.example.entity.LogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;


//实现LogUploader接口，上传文件到server的mysql数据库中
@Component("serverLogUploader")
public class ServerLogUploader implements LogUploader {
  @Autowired
  private RestTemplate restTemplate;
//  private String serverUrl = "http://localhost:9092/api/log/upload";

  @Override
  public void uploadFiles(List<LogFile> logFiles) {
    String serverUrl = "http://localhost:9092/api/log/upload";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<List<LogFile>> request = new HttpEntity<>(logFiles, headers);
    ResponseEntity<Result> response = restTemplate.postForEntity(serverUrl, request, Result.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      System.out.println("Logs uploaded successfully to server on mysql database.");
    } else {
      System.out.println("Failed to upload logs to server on mysql database.");
    }
  }
}
