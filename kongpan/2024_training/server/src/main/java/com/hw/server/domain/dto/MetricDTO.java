package com.hw.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mrk
 * @create 2024-05-30-18:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO {
    private String metric;
    private List<ValueDTO> values;
}
