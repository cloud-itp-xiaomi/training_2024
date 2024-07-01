package org.example.cotroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.common.Result;
import org.example.entity.LogEntry;
import org.example.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/log")
public class LogController {

  @Autowired
  private LogRepository logRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

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

//  @PostMapping("/uploadToLocal")
//  public ResponseEntity<Result<String>> uploadToLocal(@RequestBody List<LogEntry> logEntries) {
//    String targetFilePath = "D:/xiaomi/test/1.log";  // 固定写入路径
//    try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath, true))) {
//      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//      for (LogEntry logEntry : logEntries) {
//        StringBuilder jsonBuilder = new StringBuilder();
//        jsonBuilder.append("{\n");
//        jsonBuilder.append(String.format("\"Hostname\": \"%s\",\n", logEntry.getHostname()));
//        jsonBuilder.append(String.format("\"File\": \"%s\",\n", logEntry.getFile()));
//        // 逗号连接，引号分隔
//        if (logEntry.getContent() != null) {
//          String logsAsString = Arrays.stream(logEntry.getContent())
//                  .map(s -> "\"" + s.replace("\"", "\\\"") + "\"") // 处理特殊字符
//                  .collect(Collectors.joining(", "));
//          jsonBuilder.append(String.format("\"Logs\": [%s],\n", logsAsString));
//        } else {
//          jsonBuilder.append("\"Logs\": [],\n"); // 如果没有日志内容，则写入空数组
//        }
//        jsonBuilder.append(String.format("\"Time\": \"%s\"\n", LocalDateTime.now().format(dtf)));
//        jsonBuilder.append("}\n");
//
//        writer.write(jsonBuilder.toString());
//        writer.flush();
//      }
//      return ResponseEntity.ok(Result.success("Logs successfully uploaded to local file: " + targetFilePath));
//    } catch (IOException e) {
//      e.printStackTrace();
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//              .body(Result.error("500", "Failed to upload logs to local file: " + targetFilePath));
//    }
//  }

  @PostMapping("/uploadToLocal")
  public ResponseEntity<Result<String>> uploadToLocal(@RequestBody List<LogEntry> logEntries) {
    String targetFilePath = "D:/xiaomi/test/1.log";  // 固定写入路径
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath, true))) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

      for (LogEntry logEntry : logEntries) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append(String.format("\"Hostname\": \"%s\",\n", logEntry.getHostname()));
        jsonBuilder.append(String.format("\"File\": \"%s\",\n", logEntry.getFile().replace("\\", "\\\\")));  // 替换反斜线
        // 逗号连接，引号分隔
        if (logEntry.getContent() != null) {
          String logsAsString = Arrays.stream(logEntry.getContent())
                  .map(s -> "\"" + s.replace("\"", "\\\"") + "\"")  // 处理特殊字符
                  .collect(Collectors.joining(", "));
          jsonBuilder.append(String.format("\"Logs\": [%s],\n", logsAsString));
        } else {
          jsonBuilder.append("\"Logs\": [],\n"); // 如果没有日志内容，则写入空数组
        }
        jsonBuilder.append(String.format("\"Time\": \"%s\"\n", LocalDateTime.now().format(dtf)));
        jsonBuilder.append("}\n");

        writer.write(jsonBuilder.toString());
        writer.flush();
      }
      return ResponseEntity.ok(Result.success("Logs successfully uploaded to local file: " + targetFilePath));
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(Result.error("500", "Failed to upload logs to local file: " + targetFilePath));
    }
  }

  @GetMapping("/queryLocal")
  public ResponseEntity<?> queryLocalLogs(@RequestParam String hostname, @RequestParam String file) {
    String targetFilePath = "D:/xiaomi/test/1.log";
    ObjectMapper objectMapper = new ObjectMapper();
    StringBuilder jsonBuilder = new StringBuilder();

    try (Stream<String> stream = Files.lines(Paths.get(targetFilePath))) {
      Iterator<String> iterator = stream.iterator();
      boolean isJsonStarted = false;
      int braceCount = 0;

      while (iterator.hasNext()) {
        String line = iterator.next().trim();

        if (line.contains("{")) {
          braceCount++;
          isJsonStarted = true;
        }

        if (isJsonStarted) {
          jsonBuilder.append(line);
        }

        if (line.contains("}")) {
          braceCount--;
          if (braceCount == 0 && isJsonStarted) {

            isJsonStarted = false;
            Map<String, Object> map = objectMapper.readValue(jsonBuilder.toString(), Map.class);
            jsonBuilder.setLength(0);


            boolean a = hostname.equals((map.get("Hostname")).toString());
            boolean b = file.equals((map.get("File")).toString());

            if (hostname.equals((map.get("Hostname")).toString()) && file.equals((map.get("File")).toString())) {
              //return ResponseEntity.ok(map);
              return ResponseEntity.ok(Result.success(map));
            }
          }
        }
      }
      return ResponseEntity.notFound().build();  
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Failed to read logs from local file: " + targetFilePath);
    }
  }







}
