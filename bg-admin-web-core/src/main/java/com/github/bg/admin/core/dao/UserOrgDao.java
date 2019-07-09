package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.UserOrg;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserOrgDao extends Mapper<UserOrg> {

    /**
     * 功能描述：根据用户ID来删除关联数据
     * @param userId 用户ID
     * @return 返回删除结果
     */
    int deleteUserOrgByUserId(@Param("userId") String userId);

    /**
     * 功能描述：根据组织架构ID来统计用户和组织架构的数量
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    int countUserOrgByOrgId(@Param("orgId") Integer orgId);

    /**
     * 功能描述：根据组织架构ID来删除关联关系的数据
     * @param orgId 组织架构流水ID
     * @return 返回删除结果
     */
    int deleteUserOrgByOrgId(@Param("orgId") Integer orgId);

}