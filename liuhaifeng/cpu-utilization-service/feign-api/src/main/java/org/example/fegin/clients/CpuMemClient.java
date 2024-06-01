package org.example.fegin.clients;

import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * fegin的api接口
 *
 * @author liuhaifeng
 * @date 2024/05/30/21:12
 */
@FeignClient("cpuserver")
public interface CpuMemClient {

    @PostMapping("api/metric/upload")
    Result<Void> upload(@RequestBody List<CpuMemInfoDTO> cpuMemInfoDTOList);

    @GetMapping("api/metric/query")
    Result<List<CpuMemQueryVO>> query(@SpringQueryMap CpuMemQueryDTO cpuMemQueryDTO);
}
