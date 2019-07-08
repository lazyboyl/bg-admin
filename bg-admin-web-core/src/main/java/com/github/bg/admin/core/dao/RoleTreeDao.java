package com.github.bg.admin.core.dao;

import com.github.bg.admin.core.entity.RoleTree;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface RoleTreeDao extends Mapper<RoleTree> {

    /**
     * 功能描述：根据菜单ID删除关联关系
     * @param treeId 菜单ID
     * @return 返回删除结果
     */
    int deleteRoleTreeByTreeId(@Param("treeId")Integer treeId);

    /**
     * 功能描述：根据角色ID来删除角色菜单关联数据
     * @param roleId 角色ID
     * @return 返回删除结果
     */
    int deleteRoleTreeByRoleId(@Param("roleId")String roleId);

}