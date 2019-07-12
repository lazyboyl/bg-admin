package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.BehaviorLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BehaviorLogDao extends Mapper<BehaviorLog> {

    /**
     * 功能描述：获取行为日志列表
     *
     * @param search 模糊匹配日志的用户名字和响应方法
     * @return 返回查询结果
     */
    List<BehaviorLog> queryBehaviorLogList(@Param("search") String search);

}