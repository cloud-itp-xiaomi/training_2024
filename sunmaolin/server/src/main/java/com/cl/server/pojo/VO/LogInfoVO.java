package com.cl.server.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 日志信息VO
 *
 * @author tressures
 * @date:  2024/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInfoVO {

    private String hostname;

    private String file;

    private List<String> logs;
}
