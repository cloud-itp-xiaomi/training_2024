package org.example.test;

import org.example.common.utils.TimeFormatUtil;
import org.example.controller.UtilizationController;
import org.example.enums.MetricTypeEnum;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.fegin.pojo.vo.UtilizationQueryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * cpu-server测试类
 *
 * @author liuhaifeng
 * @date 2024/06/07/18:44
 */

@SpringBootTest
public class UtilizationServerTest {

    @Autowired
    private UtilizationController utilizationController;

    @Test
    public void queryByOneMetricTest() {
        Result<List<UtilizationQueryVO>> query = utilizationController.query("my-computer", "cpu.used.percent", 1717228800L, 1717758829L);
        System.out.println(query);
    }

    @Test
    public void queryByAllMetricTest() {
        Result<List<UtilizationQueryVO>> query = utilizationController.query("my-computer", "", 1717228800L, 1717758829L);
        System.out.println(query);
    }

    @Test
    public void cpuMemInfoUploadTest() {
        List<UtilizationUploadDTO> cpuMemInfoDTOList = new ArrayList<>();
        UtilizationUploadDTO cpuMemInfoDTO1 = UtilizationUploadDTO
                .builder()
                .metric("cpu.used.percent")
                .endpoint("my-computer")
                .timestamp(1717230027L)
                .step(60)
                .value(99.1)
                .build();
        UtilizationUploadDTO cpuMemInfoDTO2 = UtilizationUploadDTO
                .builder()
                .metric("mem.used.percent")
                .endpoint("my-computer")
                .timestamp(1717230027L)
                .step(60)
                .value(68.0)
                .build();
        cpuMemInfoDTOList.add(cpuMemInfoDTO1);
        cpuMemInfoDTOList.add(cpuMemInfoDTO2);
        Result<Void> upload = utilizationController.upload(cpuMemInfoDTOList);
        System.out.println(upload);
    }

    @Test
    public void timeFormatUtilTest() {
        LocalDateTime localDatetime = TimeFormatUtil.longToLocalDateTime(1718278904L);
        System.out.println(localDatetime);
        Assertions.assertEquals(LocalDateTime.of(2024, 6, 13, 19, 41, 44), localDatetime);
        Long longTime = TimeFormatUtil.localDateTimeToLong(localDatetime);
        System.out.println(longTime);
        Assertions.assertEquals(1718278904L, longTime.longValue());
    }

    @Test
    public void metricTypeEnumTest() {
        MetricTypeEnum cpuUsedPercent = MetricTypeEnum.getByCode(1);
        MetricTypeEnum memUsedPercent = MetricTypeEnum.getByCode(2);
        MetricTypeEnum cpuUsedPercent1 = MetricTypeEnum.getByValue("cpu.used.percent");
        MetricTypeEnum memUsedPercent1 = MetricTypeEnum.getByValue("mem.used.percent");
        Assertions.assertEquals(MetricTypeEnum.CPU_USED_PERCENT, cpuUsedPercent);
        Assertions.assertEquals(MetricTypeEnum.CPU_USED_PERCENT, cpuUsedPercent1);
        Assertions.assertEquals(MetricTypeEnum.MEM_USED_PERCENT, memUsedPercent);
        Assertions.assertEquals(MetricTypeEnum.MEM_USED_PERCENT, memUsedPercent1);
    }
}
