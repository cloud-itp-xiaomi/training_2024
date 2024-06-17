package com.txh.xiaomi2024.work.service.domian.vo;

import com.alibaba.fastjson2.JSON;
import com.txh.xiaomi2024.work.service.bean.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author txh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class LogDetailVO extends TransformVO<Log, LogDetailVO> {
    private String hostname; // 主机
    private String file; // 日志文件
    private List<String> logs; // 日志列表
    private Long last_update_time;
    @Override
    public LogDetailVO transFormDoc(Log doc) {
        // 构造目标对象
        String toJSON = JSON.toJSONString(doc);
        LogDetailVO logDetailVO = JSON.parseObject(toJSON, LogDetailVO.class);
        return logDetailVO;
    }
}
