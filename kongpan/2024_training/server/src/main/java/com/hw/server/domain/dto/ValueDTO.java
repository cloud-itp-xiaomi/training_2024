package com.hw.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mrk
 * @create 2024-05-30-20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueDTO {
    private Long timestamp;
    private Double value;
}
