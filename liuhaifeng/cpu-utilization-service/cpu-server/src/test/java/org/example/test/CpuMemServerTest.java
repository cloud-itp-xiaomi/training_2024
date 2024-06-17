package org.example.test;

import org.example.controller.CpuMemController;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * cpu-server测试类
 *
 * @author liuhaifeng
 * @date 2024/06/07/18:44
 */

@SpringBootTest
public class CpuMemServerTest {

    @Autowired
    private CpuMemController cpuMemController;

    @Test
    public void queryByOneMetricTest() {
        CpuMemQueryDTO cpuMemQueryDTO = CpuMemQueryDTO
                .builder()
                .endpoint("my-computer")
                .metric("cpu.used.percent")
                .startTs(1717228800L)
                .endTs(1717758829L)
                .build();
        Result<List<CpuMemQueryVO>> query = cpuMemController.query(cpuMemQueryDTO);
        System.out.println(query);
    }

    @Test
    public void queryByAllMetricTest() {
        CpuMemQueryDTO cpuMemQueryDTO = CpuMemQueryDTO
                .builder()
                .endpoint("my-computer")
                .metric("")
                .startTs(1717228800L)
                .endTs(1717758829L)
                .build();
        Result<List<CpuMemQueryVO>> query = cpuMemController.query(cpuMemQueryDTO);
        System.out.println(query);
    }

    @Test
    public void cpuMemInfoUploadTest() {
        List<CpuMemInfoDTO> cpuMemInfoDTOList = new ArrayList<>();
        CpuMemInfoDTO cpuMemInfoDTO1 = CpuMemInfoDTO
                .builder()
                .metric("cpu.used.percent")
                .endpoint("my-computer")
                .timestamp(1717230027L)
                .step(60)
                .value(99.1)
                .build();
        CpuMemInfoDTO cpuMemInfoDTO2 = CpuMemInfoDTO
                .builder()
                .metric("mem.used.percent")
                .endpoint("my-computer")
                .timestamp(1717230027L)
                .step(60)
                .value(68.0)
                .build();
        cpuMemInfoDTOList.add(cpuMemInfoDTO1);
        cpuMemInfoDTOList.add(cpuMemInfoDTO2);
        Result<Void> upload = cpuMemController.upload(cpuMemInfoDTOList);
        System.out.println(upload);
    }
}
