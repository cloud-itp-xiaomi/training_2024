package com.example.mi1.domain.po;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Log{
    private String hostname;
    private String file;
    private String log;
    private Long timestamp;
}
