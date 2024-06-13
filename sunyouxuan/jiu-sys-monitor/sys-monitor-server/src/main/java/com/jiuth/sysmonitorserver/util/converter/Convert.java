package com.jiuth.sysmonitorserver.util.converter;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import com.jiuth.sysmonitorserver.dto.SysInfoCaptureDTO;

import java.util.List;
import java.util.stream.Collectors;

public class Convert {

    public static SysInfoCapture toEntity(SysInfoCaptureDTO dto) {
        SysInfoCapture entity = new SysInfoCapture();
        entity.setMetric(dto.getMetric());
        entity.setEndpoint(dto.getEndpoint());
        entity.setTimestamp(dto.getTimestamp());
        entity.setStep(dto.getStep());
        entity.setValue(dto.getValue());
        return entity;
    }

    public static List<SysInfoCapture> toEntityList(List<SysInfoCaptureDTO> dtoList) {
        return dtoList.stream().map(Convert::toEntity).collect(Collectors.toList());
    }
}
