package org.example.fegin.pojo.dto;

import lombok.Data;
import org.example.fegin.pojo.CpuMem;

import java.util.List;


/**
 * 上传数据接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/05/30/0:04
 */
@Data
public class CpuMemInfoDTO {
    List<CpuMem> cpuMems;
}
