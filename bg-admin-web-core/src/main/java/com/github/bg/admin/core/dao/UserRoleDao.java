package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserRoleDao extends Mapper<UserRole> {

    /**
     * 功能描述：根据用户ID来删除关联数据
     * @param userId 用户ID
     * @return 返回删除结果
     */
    int deleteUserRoleByUserId(@Param("userId") String userId);

    /**
     * 功能描述：根据用户ID来获取用户和角色的关联数据
     *
     * @param userId 用户ID
     * @return 返回查询结果
     */
    List<UserRole> getUserRoleByUserId(@Param("userId") String userId);

}