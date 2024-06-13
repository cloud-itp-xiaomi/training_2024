package org.example.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Converter
public class JsonStringArrayConverter implements AttributeConverter<String[], String> {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(String[] attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Error converting string array to JSON", e);
    }
  }

  @Override
  public String[] convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, String[].class);
    } catch (IOException e) {
      throw new IllegalStateException("Error converting JSON to string array", e);
    }
  }
}
