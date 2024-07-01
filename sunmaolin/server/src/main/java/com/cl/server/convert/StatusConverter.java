package com.cl.server.convert;

import com.cl.server.entity.Status;
import com.cl.server.pojo.DTO.StatusDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
/**
 * Status转换器
 *
 * @author: tressures
 * @date: 2024/6/7
 */
@Mapper
public interface StatusConverter {

    StatusConverter INSTANCE = Mappers.getMapper(StatusConverter.class);

    List<Status> convertDTOListToEntityList(List<StatusDTO> statusDTOS);

}
