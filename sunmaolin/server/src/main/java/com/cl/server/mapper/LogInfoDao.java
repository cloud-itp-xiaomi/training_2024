package com.cl.server.mapper;

import com.cl.server.entity.LogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * (LogInfo)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-06 17:28:12
 */
@Mapper
public interface LogInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LogInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param logInfo 查询条件
     * @return 对象列表
     */
    List<LogInfo> queryAllByLimit(LogInfo logInfo);

    /**
     * 统计总行数
     *
     * @param logInfo 查询条件
     * @return 总行数
     */
    long count(LogInfo logInfo);

    /**
     * 新增数据
     *
     * @param logInfo 实例对象
     * @return 影响行数
     */
    int insert(LogInfo logInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LogInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogInfo> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LogInfo> entities);

    /**
     * 修改数据
     *
     * @param logInfo 实例对象
     * @return 影响行数
     */
    int update(LogInfo logInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

