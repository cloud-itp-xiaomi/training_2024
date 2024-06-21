package com.lx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMessageConsumeServiceTest {

    @Test
    void onMessage() {
        ConfigMessageConsumeService configMessageConsumeService = new ConfigMessageConsumeService();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString("local_file");
            configMessageConsumeService.onMessage(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert "local_file".equals(ConfigMessageConsumeService.logStorage);
    }
}