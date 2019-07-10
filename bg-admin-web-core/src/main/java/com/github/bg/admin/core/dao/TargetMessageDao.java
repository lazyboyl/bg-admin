package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.TargetMessage;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface TargetMessageDao extends Mapper<TargetMessage> {

    /**
     * 功能描述：实现消息的阅读
     * @param state 消息的状态
     * @param readeTime 阅读时间
     * @param targetMessageId 消息关联流水ID
     * @return 返回更新结果
     */
    int readMsg(@Param("state") String state,
                @Param("readeTime") Date readeTime,
                @Param("targetMessageId") String targetMessageId);

}