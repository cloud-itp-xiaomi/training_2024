package org.example.fegin.clients;

import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.fegin.pojo.vo.UtilizationQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * cpu和内存利用率相关接口
 *
 * @author liuhaifeng
 * @date 2024/05/30/21:12
 */
@FeignClient(name = "server", contextId = "utilizationClient")
public interface UtilizationClient {

    /**
     * 上传cpu和内存利用率
     *
     * @param utilizationUploadDTOList
     * @return
     */
    @PostMapping("api/metric/upload")
    Result<Void> upload(@RequestBody List<UtilizationUploadDTO> utilizationUploadDTOList);

    /**
     * 查询cpu和内存利用率
     *
     * @param endpoint
     * @param metric
     * @param startTs
     * @param endTs
     * @return
     */
    @GetMapping("api/metric/query")
    Result<List<UtilizationQueryVO>> query(@RequestParam("endpoint") String endpoint,
                                           @RequestParam(value = "metric", required = false) String metric,
                                           @RequestParam("start_ts") Long startTs,
                                           @RequestParam("end_ts") Long endTs);
}
