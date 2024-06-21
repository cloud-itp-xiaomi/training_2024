package org.example.cotroller;

import org.example.common.Result;
import org.example.entity.LogEntry;
import org.example.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
public class LogController {

  @Autowired
  private LogRepository logRepository;

  @PostMapping("/upload")
  public ResponseEntity<Result<String>> uploadLogs(@RequestBody LogEntry[] logEntries) {
    logRepository.saveAll(Arrays.asList(logEntries));
    // 返回成功的Result
    return ResponseEntity.ok(Result.success("Logs saved successfully to mysql database"));
  }

  @GetMapping("/query")
  public ResponseEntity<Result<List<LogEntry>>> queryLogs(
          @RequestParam(required = false) String hostname,
          @RequestParam(required = false) String file) {
    List<LogEntry> logs;
    if (hostname != null && file != null) {
      logs = logRepository.findByHostnameAndFile(hostname, file);
    } else {
      logs = logRepository.findAll();
    }
    // 返回成功的Result，data包含日志列表
    return ResponseEntity.ok(Result.success(logs));
  }


  @PostMapping("/uploadToLocal")
  public ResponseEntity<Result<String>> uploadToLocal(@RequestBody List<LogEntry> logEntries) {
    String targetFilePath = "D:/xiaomi/test/1.log";  // 固定写入路径
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath, true))) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      for (LogEntry logEntry : logEntries) {
        String currentTime = LocalDateTime.now().format(dtf);

        writer.write("Hostname: " + logEntry.getHostname() + "\n");
        writer.write("File: " + logEntry.getFile() + "\n");

        if (logEntry.getContent() != null) {
          writer.write("Content: " + Arrays.toString(logEntry.getContent()) + "\n");
        }
        writer.write("Time: " + currentTime + "\n");
        writer.newLine();
        writer.flush();
      }
      return ResponseEntity.ok(Result.success("Logs successfully uploaded to local file: " + targetFilePath));
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Result.error("500", "Failed to upload logs to local file: " + targetFilePath));
    }
  }


}
