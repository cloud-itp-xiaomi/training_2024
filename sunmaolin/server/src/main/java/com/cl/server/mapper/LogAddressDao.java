package com.cl.server.mapper;

import com.cl.server.entity.LogAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * (LogAddress)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-06 17:27:24
 */
@Mapper
public interface LogAddressDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LogAddress queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param logAddress 查询条件
     * @return 实例对象
     */
    Integer queryByLimit(LogAddress logAddress);

    /**
     * 统计总行数
     *
     * @param logAddress 查询条件
     * @return 总行数
     */
    long count(LogAddress logAddress);

    /**
     * 新增数据
     *
     * @param logAddress 实例对象
     * @return 影响行数
     */
    int insert(LogAddress logAddress);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogAddress> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LogAddress> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogAddress> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LogAddress> entities);

    /**
     * 修改数据
     *
     * @param logAddress 实例对象
     * @return 影响行数
     */
    int update(LogAddress logAddress);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

