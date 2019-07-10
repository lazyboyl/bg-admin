package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.Message;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MessageDao extends Mapper<Message> {

    /**
     * 功能描述：获取当前登录的用户的未读的消息
     * @param userId 用户ID
     * @return 返回消息数据
     */
    List<Message> queryUserMsg(@Param("userId")String userId);

    /**
     * 功能描述：查询消息数据
     * @param search 匹配的内容
     * @return 返回查询的结果
     */
    List<Message> queryMessageList(@Param("search")String search);

}