package org.example.service;

import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author liuhaifeng
 * @date 2024/05/30/21:27
 */
public interface CpuMemService {

    Result<Void> upload(@RequestBody CpuMemInfoDTO cpuMemInfoDTO);

    Result<List<CpuMemQueryVO>> query(CpuMemQueryDTO cpuMemQueryDTO);


}
