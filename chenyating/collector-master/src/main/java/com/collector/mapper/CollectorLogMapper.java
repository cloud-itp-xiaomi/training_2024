package com.collector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.collector.bean.entity.CollectorDetailLogEntity;
import com.collector.bean.entity.CollectorLogUploadEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CollectorLogMapper extends BaseMapper<CollectorLogUploadEntity> {
    int saveDetailLogInfo(@Param("logId")int logId, @Param("logs")String logs, @Param("createTime")Date createTime);

    int deleteDetailLogInfo(@Param("logId")int logId);

    List<CollectorDetailLogEntity> selectDetailLogInfo(@Param("logIds")List<Integer> logIds);
}
