package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author linzf
 * @since 2019-07-05
 * 类描述：角色的dao
 */
public interface RoleDao extends Mapper<Role> {

    /**
     * 功能描述：根据用户ID来获取该用户的相应的绑定的数据
     * @param userId 用户ID
     * @return 返回角色的集合
     */
    List<Role> getUserRoleListByUserId(@Param("userId") String userId);

}