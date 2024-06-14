package com.example.mi1.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class LogQueryVO {
    List<String> logs;
    private Integer pageNum;
    private Integer totalNum;
}
