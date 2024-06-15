package org.example.log;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Log {
    private String hostname;
    private String file;
    private String log;
    private Long timestamp;
}
