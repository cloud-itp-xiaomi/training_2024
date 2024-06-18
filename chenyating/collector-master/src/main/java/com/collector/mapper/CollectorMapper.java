package com.collector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.collector.bean.entity.CollectorUploadEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectorMapper extends BaseMapper<CollectorUploadEntity> {}
// BaseMapper 是 MyBatis-Plus (一个针对 MyBatis 的增强工具包) 中的核心接口，
// 它主要用于简化数据访问层（DAO层）的开发工作。
// 可以获得一系列 CRUD （创建 Create、检索 Read、更新 Update、删除 Delete）操作方法，
// 而无需手动编写 SQL 语句或 XML 映射文件。
