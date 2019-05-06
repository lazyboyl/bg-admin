package com.github.bg.admin.core.entity;

import javax.persistence.*;

@Table(name = "t_user_role")
public class UserRole {
    /**
     * 用户角色关联流水ID
     */
    @Id
    @Column(name = "userRoleId")
    @GeneratedValue(generator = "JDBC")
    private String userRoleId;

    /**
     * 用户ID
     */
    @Column(name = "userId")
    private String userId;

    /**
     * 角色ID
     */
    @Column(name = "roleId")
    private String roleId;

    /**
     * 获取用户角色关联流水ID
     *
     * @return userRoleId - 用户角色关联流水ID
     */
    public String getUserRoleId() {
        return userRoleId;
    }

    /**
     * 设置用户角色关联流水ID
     *
     * @param userRoleId 用户角色关联流水ID
     */
    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    /**
     * 获取用户ID
     *
     * @return userId - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取角色ID
     *
     * @return roleId - 角色ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}