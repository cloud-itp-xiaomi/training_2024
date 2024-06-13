package com.example.mi1.mapper;

import com.example.mi1.domain.UploadParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UtilizationMapper {
    @Select("SELECT * FROM cpu_utilization WHERE endpoint = #{endpoint} AND timestamp >= #{startTs} AND timestamp <= #{endTs}")
    List<UploadParam> getCPUUtilization(String endpoint, Long startTs, Long endTs);

    @Select("SELECT * FROM mem_utilization WHERE endpoint = #{endpoint} AND timestamp >= #{startTs} AND timestamp <= #{endTs}")
    List<UploadParam> getMemUtilization(String endpoint, Long startTs, Long endTs);

    @Insert("INSERT INTO cpu_utilization(endpoint, step, timestamp, value) " + "VALUES (#{endpoint}, #{step}, #{timestamp}, #{value})")
    void insertCPU(UploadParam uploadParam);

    @Insert("INSERT INTO mem_utilization(endpoint, step, timestamp, value) " + "VALUES (#{endpoint}, #{step}, #{timestamp}, #{value})")
    void insertMem(UploadParam uploadParam);

    @Select("SELECT endpoint FROM hostname")
    List<String> getEndpoints();

    @Select("SELECT file FROM filename")
    List<String> getFiles();

    @Select("SELECT endpoint FROM hostname WHERE endpoint = #{endpoint} LIMIT 1")
    String getEndpoint(String endpoint);

    @Select("SELECT file FROM filename WHERE file = #{file} LIMIT 1")
    String getFile(String file);

    @Insert("INSERT INTO hostname(endpoint) " + "VALUES (#{endpoint})")
    void insertEndpoint(String endpoint);

    @Insert("INSERT INTO filename(file) " + "VALUES (#{file)})")
    void insertFile(String file);
}
