package com.cl.server.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 状态信息VO
 *
 * @author tressures
 * @date:  2024/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResp {

    private String metric;

    private List<Values> values;
}
