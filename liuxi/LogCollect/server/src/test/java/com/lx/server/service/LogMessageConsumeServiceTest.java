package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.server.pojo.LogMessage;
import com.lx.server.utils.GetBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Component
@DependsOn(value = "getBeanUtil")
class LogMessageConsumeServiceTest {

    @Autowired
    private GetBeanUtil getBeanUtil;

    @Test
    void onMessage() {
        LogMessageConsumeService logMessageConsumeService = new LogMessageConsumeService();
        LogMessage logMessage = new LogMessage();
        logMessage.setHostName("123");
        logMessage.setFile("a.log");
        List<String> logs = new ArrayList<>();
        logs.add("hello~");
        logMessage.setLogs(logs);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(logMessage);
            logMessageConsumeService.onMessage(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotNull(logMessage);
    }
}