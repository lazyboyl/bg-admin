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

    /**
     * 功能描述：更新角色信息
     *
     * @param roleId   角色流水ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回操作结果
     */
    int updateRole(@Param("roleId") String roleId, @Param("roleName") String roleName, @Param("roleCode") String roleCode);

    /**
     * 功能描述：验证角色编码和角色名字是否重复
     *
     * @param roleId   角色ID
     * @param roleName 角色名字
     * @param roleCode 角色编码
     * @return 返回验证结果
     */
    int checkRoleCodeAndName(@Param("roleId") String roleId, @Param("roleName") String roleName, @Param("roleCode") String roleCode);

    /**
     * 功能描述：获取角色列表数据
     *
     * @param search 模糊匹配角色的roleName和roleCode
     * @return 返回查询结果
     */
    List<Role> queryRoleList(@Param("search") String search);
}