package com.cl.server.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 日志上报DTO
 *
 * @author tressures
 * @date:  2024/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInfoDTO {

    private String hostname;

    private String file;

    private List<String> logs;
}
