package com.example.demo01.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class MetricForm {//提交的数据

    @NotBlank(message = "指标名称不能为空")
    public String metric;

    @NotBlank(message = "主机名称")
    private String endpoint;

    @NotNull(message = "采集时间")
    @Min(value = 0, message = "采集时间不能<0")
    private Long timestamp;

    @NotNull(message = "采集频率不能为空")
    @Min(value = 1, message = "采集频率不能<0s")
    @Max(value = 999999, message = "采集频率不能>999999s")
    private Integer step;

    @NotNull(message = "指标数据利用率")
    @Min(value = 0, message = "利用率不能<0")
    @Max(value = 100, message = "利用率不能>100")
    private BigDecimal value;

}
