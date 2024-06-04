package com.cl.server.mapper;

import com.cl.server.entity.CpuStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * (CpuStatus)表数据库访问层
 *
 * @author tressures
 * @date:  2024-05-26 17:05:56
 */
@Mapper
public interface CpuStatusDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CpuStatus queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param cpuStatus 查询条件
     * @return 对象列表
     */
    List<CpuStatus> queryAllByLimit(CpuStatus cpuStatus);

    /**
     * 时间范围查询
     *
     * @param
     * @return 对象列表
     */
    List<CpuStatus> queryAllByTimeStamp(String endPoint,String metric,Long start_ts,Long end_ts);

    /**
     * 统计总行数
     *
     * @param cpuStatus 查询条件
     * @return 总行数
     */
    long count(CpuStatus cpuStatus);

    /**
     * 新增数据
     *
     * @param cpuStatus 实例对象
     * @return 影响行数
     */
    int insert(CpuStatus cpuStatus);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CpuStatus> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CpuStatus> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CpuStatus> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<CpuStatus> entities);

    /**
     * 修改数据
     *
     * @param cpuStatus 实例对象
     * @return 影响行数
     */
    int update(CpuStatus cpuStatus);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

